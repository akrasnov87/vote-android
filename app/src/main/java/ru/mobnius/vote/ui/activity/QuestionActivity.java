package ru.mobnius.vote.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.tools.CommentDialogFragment;
import ru.mobnius.vote.ui.fragment.tools.ContactDialogFragment;
import ru.mobnius.vote.ui.fragment.data.OnAnswerListener;
import ru.mobnius.vote.ui.fragment.data.OnQuestionListener;
import ru.mobnius.vote.ui.fragment.data.OnVoteListener;
import ru.mobnius.vote.ui.fragment.VoteItemFragment;
import ru.mobnius.vote.ui.fragment.data.onClickVoteItemListener;
import ru.mobnius.vote.ui.fragment.form.BaseFormActivity;

public class QuestionActivity extends BaseFormActivity
        implements OnVoteListener, onClickVoteItemListener, OnAnswerListener {
    public static String TAG = "QUESTIONS";
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

        mDocumentManager = new DocumentManager(this);
        mVoteManager = new VoteManager();
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
                startActivityForResult(PointInfoActivity.newIntent(this, pointID),
                        PointInfoActivity.POINT_INFO_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isDone()) {
            DataManager dataManager = DataManager.getInstance();
            List<Results> results = dataManager.getPointResults(pointID);
            // задание ранее выполнялось
            mVoteManager.importFromResult(results.toArray(new Results[0]));
        }

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

    @Override
    public void onClickVoteItem(Answer answer) {
        if (!isDone() && !mVoteManager.isQuestionExists(answer.f_question)) {
            // если вопрос ранее не задавался, то сохраняем в стэке
            mVoteManager.addQuestion(answer.f_question, answer.id, mVoteManager.getList().length);
        }

        if (mVoteManager.isExistsCommand(answer, Command.COMMENT)) {
            CommentDialogFragment commentFragment = new CommentDialogFragment(answer, mVoteManager.getComment(answer.f_question), isDone());
            commentFragment.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (mVoteManager.isExistsCommand(answer, Command.CONTACT)) {
            ContactDialogFragment fragment = new ContactDialogFragment(answer, mVoteManager.getTel(answer.f_question), isDone());
            fragment.show(getSupportFragmentManager(), "dialog");
            return;
        }

        onAnswerCommand(Command.NONE, answer, null);
    }


    @Override
    public void onBackPressed() {
        long lastQuestionID = isDone() ? mVoteManager.getPrevQuestionID(mCurrentQuestionID) : mVoteManager.getLastQuestionID();
        if (lastQuestionID > 0) {
            if (!isDone()) {
                // если вопрос ранее не задавался, то удаляем из стэка последний
                mVoteManager.removeQuestion(lastQuestionID);
            }
            onShowQuestion(lastQuestionID);
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

    private OnQuestionListener getLastQuestionListener() {
        return (OnQuestionListener) getSupportFragmentManager().findFragmentById(R.id.single_fragment_container);
    }

    /**
     * Вывод вопроса
     * @param questionID иден. вопроса
     */
    private void onShowQuestion(long questionID) {
        mCurrentQuestionID = questionID;
        long exclusionAnswerID = getVoteManager().getQuestionAnswer(questionID);
        getLastQuestionListener().onQuestionBind(questionID, exclusionAnswerID);
    }

    /**
     *
     * @param answer Обработанный ранее ответ
     */
    @Override
    public void onAnswerCommand(String type, Answer answer, Object result) {
        switch (type) {
            case Command.COMMENT:
                mVoteManager.updateQuestion(answer.f_question, String.valueOf(result), null);
                break;

            case Command.CONTACT:
                mVoteManager.updateQuestion(answer.f_question, null, String.valueOf(result));
                break;
        }

        // Присутствует команда Завершения
        if (mVoteManager.isExistsCommand(answer, Command.FINISH)) {
            if (isDone()) {
                finish();
            } else {
                onVoteFinish();
            }
            return;
        }

        if (answer.f_next_question > 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            VoteItemFragment fragment = VoteItemFragment.createInstance();
            fragmentManager.beginTransaction().
                    replace(R.id.single_fragment_container, fragment).commitNow();
            onShowQuestion(answer.f_next_question);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PointInfoActivity.POINT_INFO_CODE && resultCode == RESULT_OK) {
            mVoteManager.clear();
        }
    }
}
