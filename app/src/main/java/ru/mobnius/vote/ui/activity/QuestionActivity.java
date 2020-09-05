package ru.mobnius.vote.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.Command;
import ru.mobnius.vote.Names;
import ru.mobnius.vote.R;
import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.DocumentManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.exception.IExceptionCode;
import ru.mobnius.vote.data.manager.vote.VoteManager;
import ru.mobnius.vote.data.storage.models.Answer;
import ru.mobnius.vote.data.storage.models.Question;
import ru.mobnius.vote.data.storage.models.Results;
import ru.mobnius.vote.ui.fragment.tools.AnswerFragmentDialog;
import ru.mobnius.vote.ui.fragment.tools.CommentDialogFragment;
import ru.mobnius.vote.ui.fragment.tools.ContactDialogFragment;
import ru.mobnius.vote.ui.data.OnAnswerListener;
import ru.mobnius.vote.ui.data.OnQuestionListener;
import ru.mobnius.vote.ui.data.OnVoteListener;
import ru.mobnius.vote.ui.fragment.VoteItemFragment;
import ru.mobnius.vote.ui.data.OnClickVoteItemListener;
import ru.mobnius.vote.ui.BaseFormActivity;
import ru.mobnius.vote.ui.fragment.tools.RatingDialogFragment;
import ru.mobnius.vote.ui.fragment.tools.VoteInHomeDialogFragment;
import ru.mobnius.vote.ui.fragment.tools.VotingDialogFragment;
import ru.mobnius.vote.ui.model.FeedbackExcessData;
import ru.mobnius.vote.ui.model.PointInfo;
import ru.mobnius.vote.ui.model.PointItem;
import ru.mobnius.vote.utils.AuditUtils;

public class QuestionActivity extends BaseFormActivity
        implements OnVoteListener, OnClickVoteItemListener, OnAnswerListener {

    public static final int QUESTION_REQUEST_CODE = 2;

    private static final String ANSWER_ID = "answer_id";
    private static final String QUESTION_ID = "question_id";
    private static final String VOTE = "vote";

    private static final String TAG = "QUESTIONS";
    private Menu actionMenu;
    private VoteManager mVoteManager;
    private DocumentManager mDocumentManager;

    private String routeID;
    private String pointID;
    private long mCurrentQuestionID;
    private long mLastAnswerID = -1;
    private PointInfo mPointInfo;
    private AnswerFragmentDialog mDialog;

    /**
     * Создание нового результата
     *
     * @param pointItem точка маршрута
     */
    public static Intent newIntent(Context context, PointItem pointItem) {
        Intent intent = new Intent(context, QuestionActivity.class);
        intent.putExtra(Names.POINT_ID, pointItem.id);
        intent.putExtra(Names.ROUTE_ID, pointItem.routeId);
        intent.putExtra(Names.NAME, pointItem.appartament);
        intent.putExtra(Names.ADDRESS, String.format("%s д. %s", pointItem.address, pointItem.houseNumber));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_container);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.single_fragment_container, VoteItemFragment.createInstance())
                .commit();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getIntent().getStringExtra(Names.NAME));
        Objects.requireNonNull(getSupportActionBar()).setSubtitle(getIntent().getStringExtra(Names.ADDRESS));

        mDocumentManager = new DocumentManager(this);

        if(savedInstanceState == null) {
            pointID = getIntent().getStringExtra(Names.POINT_ID);
            routeID = getIntent().getStringExtra(Names.ROUTE_ID);
            mVoteManager = new VoteManager();
            mPointInfo = DataManager.getInstance().getPointInfo(pointID);
            AuditUtils.write(pointID, AuditUtils.VOTE, AuditUtils.Level.LOW);
        } else {
            pointID = savedInstanceState.getString(Names.POINT_ID);
            mPointInfo = DataManager.getInstance().getPointInfo(pointID);
            routeID = savedInstanceState.getString(Names.ROUTE_ID);
            mVoteManager = (VoteManager)savedInstanceState.getSerializable(VOTE);
            mCurrentQuestionID = savedInstanceState.getLong(QUESTION_ID);
            mLastAnswerID = savedInstanceState.getLong(ANSWER_ID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choice_document, menu);
        MenuItem actionGeo = menu.findItem(R.id.choice_document_geo);
        actionGeo.setVisible(!isDone());

        //MenuItem feedback = menu.findItem(R.id.choice_document_feedback);
        //feedback.setVisible(!isDone());

        actionMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.choice_document_feedback:
                // EXCESS_DATA
                // CHANGE_NUMBER
                new AlertDialog.Builder(this).setMessage("О чем Вы хотите сообщить?").setNeutralButton("Другое", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(FeedbackActivity.getIntent(getBaseContext(), FeedbackActivity.QUESTION, new FeedbackExcessData(pointID, getIntent().getStringExtra(Names.NAME), getIntent().getStringExtra(Names.ADDRESS)).toString()));
                    }
                })
                .setPositiveButton("Нет квартиры", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(FeedbackActivity.getIntent(getBaseContext(), FeedbackActivity.EXCESS_DATA, new FeedbackExcessData(pointID, getIntent().getStringExtra(Names.NAME), getIntent().getStringExtra(Names.ADDRESS)).toString()));
                    }
                }).setNegativeButton("Изменить номер", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(FeedbackActivity.getIntent(getBaseContext(), FeedbackActivity.CHANGE_NUMBER, new FeedbackExcessData(pointID, getIntent().getStringExtra(Names.NAME), getIntent().getStringExtra(Names.ADDRESS)).toString()));
                    }
                }).create().show();
                return true;

            case R.id.choice_document_info:
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

        Question question;
        if(Authorization.getInstance().getUser().isCandidate()) {
            question = DataManager.getInstance().getQuestions(mPointInfo.getPriority())[0];
        } else {
            question = DataManager.getInstance().getQuestions()[0];
        }

        onShowQuestion(mCurrentQuestionID > 0 ? mCurrentQuestionID : question.id,
                mLastAnswerID > 0 ? mLastAnswerID : -1);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Names.ROUTE_ID, routeID);
        outState.putString(Names.POINT_ID, pointID);
        outState.putLong(QUESTION_ID, mCurrentQuestionID);
        mVoteManager.removeQuestion(mCurrentQuestionID);
        outState.putSerializable(VOTE, mVoteManager);
        if(isDone()) {
            outState.putLong(ANSWER_ID, mLastAnswerID);
        } else {
            outState.putLong(ANSWER_ID, -1);
        }
    }

    @Override
    public int getExceptionCode() {
        return IExceptionCode.QUESTION;
    }

    /**
     * Обработчик получения координат
     *
     * @param status    статус сигнала: GeoListener.NONE, GeoListener.NORMAL, GeoListener.GOOD
     */
    @Override
    public void onLocationStatusChange(int status, Location location) {
        super.onLocationStatusChange(status, location);

        Log.d(TAG, "Статус: " + status);

        if (actionMenu != null) {
            int icon;
            String message;
            switch (status) {
                case BaseFormActivity.NONE:
                    icon = R.drawable.ic_gps_off_24px;
                    message = "Местоположение не определено.";
                    break;

                case BaseFormActivity.NORMAL:
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
        if (!isDone()) {
            if(mVoteManager.isQuestionExists(answer.f_question)) {
                // если вопрос ранее задавался, то удаляем из стэка
                mVoteManager.removeQuestion(answer.f_question);
            }
            // если вопрос ранее не задавался, то сохраняем в стэке
            mVoteManager.addQuestion(answer.f_question, answer.id, mVoteManager.getList().length);
        }

        if (mVoteManager.isExistsCommand(answer, Command.COMMENT)) {
            mDialog = new CommentDialogFragment(answer, mVoteManager.getComment(answer.f_question), isDone());
            mDialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (mVoteManager.isExistsCommand(answer, Command.CONTACT)) {
            mDialog = new ContactDialogFragment(answer, mVoteManager.getTel(answer.f_question), isDone());
            mDialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (mVoteManager.isExistsCommand(answer, Command.VOTING)) {
            mDialog = new VotingDialogFragment(answer, mVoteManager.getTel(answer.f_question), isDone());
            mDialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (mVoteManager.isExistsCommand(answer, Command.VOTE_IN_HOME)) {
            mDialog = new VoteInHomeDialogFragment(answer, mVoteManager.getTel(answer.f_question), isDone());
            mDialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        if (mVoteManager.isExistsCommand(answer, Command.RATING)) {
            mDialog = new RatingDialogFragment(answer, mVoteManager.getRating(answer.f_question), isDone());
            mDialog.show(getSupportFragmentManager(), "dialog");
            return;
        }

        onAnswerCommand(Command.NONE, answer, null);
    }

    @Override
    public void onBackPressed() {
        long lastQuestionID = isDone() ? mVoteManager.getPrevQuestionID(mCurrentQuestionID) : mVoteManager.getLastQuestionID();
        if (lastQuestionID > 0) {
            long lastAnswerId = -1;
            if (!isDone()) {
                lastAnswerId = mVoteManager.getQuestionAnswer(lastQuestionID);
                // если вопрос ранее не задавался, то удаляем из стэка последний
                mVoteManager.removeQuestion(lastQuestionID);
            }
            onShowQuestion(lastQuestionID, lastAnswerId);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public String getHelpKey() {
        return "question";
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
        AuditUtils.write(pointID, AuditUtils.VOTED, AuditUtils.Level.LOW);
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
    private void onShowQuestion(long questionID, long lastAnswerId) {
        mLastAnswerID = lastAnswerId;
        mCurrentQuestionID = questionID;
        long exclusionAnswerID = getVoteManager().getQuestionAnswer(questionID);
        getLastQuestionListener().onQuestionBind(mPointInfo, questionID, exclusionAnswerID, lastAnswerId);
    }

    /**
     *
     * @param answer Обработанный ранее ответ
     */
    @Override
    public void onAnswerCommand(String type, Answer answer, Object result) {
        switch (type) {
            case Command.COMMENT:
                mVoteManager.updateQuestion(answer.f_question, String.valueOf(result), null, null);
                break;

            case Command.CONTACT:
                mVoteManager.updateQuestion(answer.f_question, null, String.valueOf(result), null);
                break;

            case Command.VOTING:
                mVoteManager.updateQuestion(answer.f_question, null, String.valueOf(result), null);

                if (mVoteManager.isExistsCommand(answer, Command.RATING)) {
                    mDialog = new RatingDialogFragment(answer, mVoteManager.getRating(answer.f_question), isDone());
                    mDialog.show(getSupportFragmentManager(), "dialog");
                    return;
                }
                break;

            case Command.VOTE_IN_HOME:
                int rating = 1;
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
                    if(jsonObject.has("n_rating")) {
                        rating = jsonObject.getInt("n_rating");
                    }
                } catch (JSONException e) {
                    Logger.error(e);
                }
                mVoteManager.updateQuestion(answer.f_question, null, String.valueOf(result), rating);
                break;

            case Command.RATING:
                mVoteManager.updateQuestion(answer.f_question, null, null, Integer.parseInt(String.valueOf(result)));
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
            onShowQuestion(answer.f_next_question, -1);
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
