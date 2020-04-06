package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.DocumentManager;
import ru.mobnius.vote.data.manager.GeoManager;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.vote.VoteManager;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.DaoSession;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.additional.CommentDialogFragment;
import ru.mobnius.vote.ui.fragment.additional.ContactDialogFragment;
import ru.mobnius.vote.ui.fragment.data.onQuestionListener;
import ru.mobnius.vote.ui.fragment.data.OnVoteListener;
import ru.mobnius.vote.ui.fragment.additional.BaseAdditionalInfoDialog;
import ru.mobnius.vote.ui.fragment.VoteItemFragment;
import ru.mobnius.vote.ui.fragment.data.onClickVoteItemListener;
import ru.mobnius.vote.ui.fragment.form.BaseFormActivity;

public class QuestionActivity extends BaseFormActivity
        implements OnVoteListener, onClickVoteItemListener, BaseAdditionalInfoDialog.IAdditionalInfoCallback {
    public static String TAG = "METER_READINGS";
    private Menu actionMenu;
    private VoteManager mVoteManager;
    private DocumentManager mDocumentManager;

    private String routeID;
    private String pointID;
    private long mCurrentQuestionID;

    /**
     * Создание нового результата
     *
     * @param context
     * @param routeId маршрут, Routes
     * @param pointId точка маршрута, Points
     * @return
     */
    public static Intent newIntent(Context context, String routeId, String pointId) {
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(Names.POINT_ID, pointId);
        intent.putExtra(Names.ROUTE_ID, routeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return VoteItemFragment.createInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        pointID = getIntent().getStringExtra(Names.POINT_ID);
        routeID = getIntent().getStringExtra(Names.ROUTE_ID);

        mDocumentManager = new DocumentManager((OnVoteListener) this);

        mVoteManager = new VoteManager();
        if (isDone()) {
            DataManager dataManager = DataManager.getInstance();
            List<Results> results = dataManager.getPointResults(pointID);
            // задание ранее выполнялось
            mVoteManager.importFromResult(results.toArray(new Results[0]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choice_document, menu);
        actionMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.choiceDocument_Info:
                if (getIntent().hasExtra(Names.RESULT_ID)) {
                    DaoSession daoSession = DataManager.getInstance().getDaoSession();
                    String resultId = getIntent().getStringExtra(Names.RESULT_ID);
                    Results mResult = daoSession.getResultsDao().load(resultId);
                    startActivity(PointInfoActivity.newIntent(this, mResult.fn_point));
                } else {
                    startActivity(PointInfoActivity.newIntent(this, getIntent().getStringExtra(Names.POINT_ID)));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Question question = DataManager.getInstance().getQuestions()[0];
        onShowQuestion(question.id);
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.CONTROL_METER_READINGS;
    }

    /**
     * Обработчик получения координат
     *
     * @param status    статус сигнала: GeoListener.NONE, GeoListener.NORMAL, GeoListener.GOOD
     * @param latitude
     * @param longitude
     */
    @Override
    public void onLocationStatusChange(int status, double latitude, double longitude) {
        Log.d(TAG, "Статус: " + status);

        if (actionMenu != null) {
            int icon = -1;
            if (actionMenu != null) {
                String message = "";
                switch (status) {
                    case GeoManager.GeoListener.NONE:
                        icon = R.drawable.ic_gps_off_24px;
                        message = "Местоположение не определено.";
                        break;

                    case GeoManager.GeoListener.NORMAL:
                        icon = R.drawable.ic_gps_not_fixed_24px;
                        message = "Координата не является точной.";
                        break;

                    default:
                        icon = R.drawable.ic_gps_fixed_24px;
                        message = "Координата получена.";
                        break;
                }

                final int MENU_GPS_ITEM = 0;
                MenuItem menuItem = actionMenu.getItem(MENU_GPS_ITEM);
                menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

                menuItem.setIcon(icon);
                menuItem.setTitle(message);
            }
        }
    }

    @Override
    public void onClickVoteItem(Answer answer) {
        if (!isDone()) {
            // если вопрос ранее не задавался, то сохраняем в стэке
            mVoteManager.addQuestion(answer.f_question, answer.id, mVoteManager.getList().length);
        }

        if (answer.f_next_question > 0) {
            onShowQuestion(answer.f_next_question);
        }
        if (answer.c_action.contains(Command.COMMENT)) {
            CommentDialogFragment commentFragment;
            if (answer.c_action.contains(Command.FINISH)) {
                commentFragment = new CommentDialogFragment(false, true);
            } else {
                commentFragment = new CommentDialogFragment(false, false);
            }
            commentFragment.show(getSupportFragmentManager(), "dialog");
            return;
        }

        // Присутствует команда Завершения
        if (answer.c_action.contains(Command.FINISH)) {
            if (isDone()) {
                finish();
            } else {
                onVoteFinish();
            }
            return;
        }

        if (answer.c_action.contains(Command.CONTACT)) {
            ContactDialogFragment fragment = new ContactDialogFragment(false);
            fragment.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (answer.f_next_question > 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            VoteItemFragment fragment = VoteItemFragment.createInstance();
            fragmentManager.beginTransaction().replace(R.id.single_fragment_container, fragment);
            mVoteManager.addQuestion(answer.f_question, answer.id, answer.n_order);
        }


    }


    @Override
    public void onBackPressed() {
        long lastQuestionID = isDone() ? mVoteManager.getPrevQuestionID(mCurrentQuestionID) : mVoteManager.getLastQuestionID();
        if (lastQuestionID > 0) {
            onShowQuestion(lastQuestionID);

            if (!isDone()) {
                // если вопрос ранее не задавался, то удаляем из стэка последнй
                mVoteManager.removeQuestion(lastQuestionID);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public VoteManager getVoteManager() {
        return mVoteManager;
    }

    @Override
    public String getRouteId() {
        return routeID;
    }

    @Override
    public String getPointId() {
        return pointID;
    }

    /**
     * Выполнялся ли ранее опрос
     *
     * @return true - да
     */
    private boolean isDone() {
        return DataManager.getInstance().getPointState(getPointId()).isDone();
    }

    /**
     * Завершение опроса
     */
    private void onVoteFinish() {
        mDocumentManager.saveVote(this);
        finish();
    }

    /**
     * Интерфейс привязки данных к вопросу
     *
     * @return
     */
    private onQuestionListener getQuestionListener() {
        return (onQuestionListener) getFragment();
    }

    /**
     * Вывод вопроса
     *
     * @param questionID иден. вопроса
     */
    private void onShowQuestion(long questionID) {
        mCurrentQuestionID = questionID;
        long exclusionAnswerID = getVoteManager().getQuestionAnswer(questionID);
        getQuestionListener().onQuestionBind(questionID, exclusionAnswerID);
    }

    /**
     * @param comment - комментарий, введенный в диалоговом окне
     */
    @Override
    public void OnCommentFinish(String comment, boolean isFinish) {
        if (isFinish) {
            if (isDone()) {
                finish();
            } else {
                onVoteFinish();
            }
        } else {

        }
    }

    /**
     * @param contacts - список контактов, добавленных в диалоговм окне
     */
    @Override
    public void OnContactFinish(ArrayList<HashMap<String, String>> contacts) {
        String x = contacts.get(0).get(ContactDialogFragment.CONTACT_NAME);
        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
    }

}
