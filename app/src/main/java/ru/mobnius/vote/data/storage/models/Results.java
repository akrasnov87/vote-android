package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings({"unused", "StringEquality"})
@Entity(nameInDb = "cd_results")
public class Results implements IEntityTo {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String fn_route;

    @ToOne(joinProperty = "fn_route")
    private UserPoints route;

    @Expose
    public String fn_point;

    /**
     * Точка
     */
    @Expose
    public String fn_user_point;

    @ToOne(joinProperty = "fn_user_point")
    private UserPoints userPoint;

    /**
     * Тип результата
     */
    @Expose
    public long fn_type;

    /**
     * Пользователь
     */
    @Expose
    public long fn_user;

    /**
     * Пользователь
     */
    @Expose
    public long fn_question;

    @ToOne(joinProperty = "fn_question")
    private Question question;

    /**
     * Пользователь
     */
    @Expose
    public long fn_answer;

    @ToOne(joinProperty = "fn_answer")
    private Answer answer;

    /**
     * Дата события
     */
    @Expose
    public String d_date;

    /**
     * Примечание
     */
    @Expose
    public String c_notice;

    /**
     * Предупреждение о возможной не достоверности
     */
    @Expose
    public boolean b_warning;

    @Expose
    public int n_order;

    @Expose
    public Integer n_rating;

    /**
     * Тип операции надл объектом
     */
    public String objectOperationType;

    /**
     * Запись была удалена или нет
     */
    private boolean isDelete;

    /**
     * Была произведена синхронизация или нет
     */
    public boolean isSynchronization;

    /**
     * идентификатор транзакции
     */
    private String tid;

    /**
     * идентификатор блока
     */
    private String blockTid;

    @Expose
    public String jb_data;

    private String dx_created;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1445092935)
    private transient ResultsDao myDao;

    @Generated(hash = 1293439524)
    public Results(String id, String fn_route, String fn_point,
            String fn_user_point, long fn_type, long fn_user, long fn_question,
            long fn_answer, String d_date, String c_notice, boolean b_warning,
            int n_order, Integer n_rating, String objectOperationType,
            boolean isDelete, boolean isSynchronization, String tid,
            String blockTid, String jb_data, String dx_created) {
        this.id = id;
        this.fn_route = fn_route;
        this.fn_point = fn_point;
        this.fn_user_point = fn_user_point;
        this.fn_type = fn_type;
        this.fn_user = fn_user;
        this.fn_question = fn_question;
        this.fn_answer = fn_answer;
        this.d_date = d_date;
        this.c_notice = c_notice;
        this.b_warning = b_warning;
        this.n_order = n_order;
        this.n_rating = n_rating;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
        this.jb_data = jb_data;
        this.dx_created = dx_created;
    }

    @Generated(hash = 991898843)
    public Results() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFn_route() {
        return this.fn_route;
    }

    public void setFn_route(String fn_route) {
        this.fn_route = fn_route;
    }

    public String getFn_point() {
        return this.fn_point;
    }

    public void setFn_point(String fn_point) {
        this.fn_point = fn_point;
    }

    public String getFn_user_point() {
        return this.fn_user_point;
    }

    public void setFn_user_point(String fn_user_point) {
        this.fn_user_point = fn_user_point;
    }

    public long getFn_type() {
        return this.fn_type;
    }

    public void setFn_type(long fn_type) {
        this.fn_type = fn_type;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public long getFn_question() {
        return this.fn_question;
    }

    public void setFn_question(long fn_question) {
        this.fn_question = fn_question;
    }

    public long getFn_answer() {
        return this.fn_answer;
    }

    public void setFn_answer(long fn_answer) {
        this.fn_answer = fn_answer;
    }

    public String getD_date() {
        return this.d_date;
    }

    public void setD_date(String d_date) {
        this.d_date = d_date;
    }

    public String getC_notice() {
        return this.c_notice;
    }

    public void setC_notice(String c_notice) {
        this.c_notice = c_notice;
    }

    public boolean getB_warning() {
        return this.b_warning;
    }

    public void setB_warning(boolean b_warning) {
        this.b_warning = b_warning;
    }

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }

    public Integer getN_rating() {
        return this.n_rating;
    }

    public void setN_rating(Integer n_rating) {
        this.n_rating = n_rating;
    }

    public String getObjectOperationType() {
        return this.objectOperationType;
    }

    public void setObjectOperationType(String objectOperationType) {
        this.objectOperationType = objectOperationType;
    }

    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean getIsSynchronization() {
        return this.isSynchronization;
    }

    public void setIsSynchronization(boolean isSynchronization) {
        this.isSynchronization = isSynchronization;
    }

    public String getTid() {
        return this.tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getBlockTid() {
        return this.blockTid;
    }

    public void setBlockTid(String blockTid) {
        this.blockTid = blockTid;
    }

    public String getJb_data() {
        return this.jb_data;
    }

    public void setJb_data(String jb_data) {
        this.jb_data = jb_data;
    }

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
    }

    @Generated(hash = 603420700)
    private transient String route__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 218525861)
    public UserPoints getRoute() {
        String __key = this.fn_route;
        if (route__resolvedKey == null || route__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserPointsDao targetDao = daoSession.getUserPointsDao();
            UserPoints routeNew = targetDao.load(__key);
            synchronized (this) {
                route = routeNew;
                route__resolvedKey = __key;
            }
        }
        return route;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1861761853)
    public void setRoute(UserPoints route) {
        synchronized (this) {
            this.route = route;
            fn_route = route == null ? null : route.getId();
            route__resolvedKey = fn_route;
        }
    }

    @Generated(hash = 1584222993)
    private transient String userPoint__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1107015340)
    public UserPoints getUserPoint() {
        String __key = this.fn_user_point;
        if (userPoint__resolvedKey == null || userPoint__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserPointsDao targetDao = daoSession.getUserPointsDao();
            UserPoints userPointNew = targetDao.load(__key);
            synchronized (this) {
                userPoint = userPointNew;
                userPoint__resolvedKey = __key;
            }
        }
        return userPoint;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1948866780)
    public void setUserPoint(UserPoints userPoint) {
        synchronized (this) {
            this.userPoint = userPoint;
            fn_user_point = userPoint == null ? null : userPoint.getId();
            userPoint__resolvedKey = fn_user_point;
        }
    }

    @Generated(hash = 527827701)
    private transient Long question__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 873859214)
    public Question getQuestion() {
        long __key = this.fn_question;
        if (question__resolvedKey == null || !question__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionDao targetDao = daoSession.getQuestionDao();
            Question questionNew = targetDao.load(__key);
            synchronized (this) {
                question = questionNew;
                question__resolvedKey = __key;
            }
        }
        return question;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1392251698)
    public void setQuestion(@NotNull Question question) {
        if (question == null) {
            throw new DaoException(
                    "To-one property 'fn_question' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.question = question;
            fn_question = question.getId();
            question__resolvedKey = fn_question;
        }
    }

    @Generated(hash = 1281831381)
    private transient Long answer__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1220172236)
    public Answer getAnswer() {
        long __key = this.fn_answer;
        if (answer__resolvedKey == null || !answer__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerDao targetDao = daoSession.getAnswerDao();
            Answer answerNew = targetDao.load(__key);
            synchronized (this) {
                answer = answerNew;
                answer__resolvedKey = __key;
            }
        }
        return answer;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 471771674)
    public void setAnswer(@NotNull Answer answer) {
        if (answer == null) {
            throw new DaoException(
                    "To-one property 'fn_answer' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.answer = answer;
            fn_answer = answer.getId();
            answer__resolvedKey = fn_answer;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 455462623)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getResultsDao() : null;
    }

}
