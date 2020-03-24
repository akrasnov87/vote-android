package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "cd_feedbacks")
public class Feedbacks implements IEntityTo {
    /**
     * Идентификатор
     */
    @Id
    @Expose
    public String id;

    @Expose
    public String c_imei;

    @Expose
    public long fn_user;

    @ToOne(joinProperty = "fn_user")
    public Users user;

    @Expose
    public long fn_type;

    @ToOne(joinProperty = "fn_type")
    public FeedbackTypes type;

    @Expose
    public String c_question;

    @Expose
    public String d_date_question;

    @Expose
    public String fn_question_file;

    @ToOne(joinProperty = "fn_question_file")
    public Files questionFile;

    @Expose
    public String c_answer;

    @Expose
    public String d_date_answer;

    @Expose
    public String fn_answer_file;

    @ToOne(joinProperty = "fn_answer_file")
    public Files answerFile;

    public String dx_created;

    /**
     * Тип операции надл объектом
     */
    public String objectOperationType;

    /**
     * Запись была удалена или нет
     */
    public boolean isDelete;

    /**
     * Была произведена синхронизация или нет
     */
    public boolean isSynchronization;

    /**
     * идентификатор транзакции
     */
    public String tid;

    /**
     * идентификатор блока
     */
    public String blockTid;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1592615615)
    private transient FeedbacksDao myDao;

    @Generated(hash = 1354953802)
    public Feedbacks(String id, String c_imei, long fn_user, long fn_type,
            String c_question, String d_date_question, String fn_question_file,
            String c_answer, String d_date_answer, String fn_answer_file,
            String dx_created, String objectOperationType, boolean isDelete,
            boolean isSynchronization, String tid, String blockTid) {
        this.id = id;
        this.c_imei = c_imei;
        this.fn_user = fn_user;
        this.fn_type = fn_type;
        this.c_question = c_question;
        this.d_date_question = d_date_question;
        this.fn_question_file = fn_question_file;
        this.c_answer = c_answer;
        this.d_date_answer = d_date_answer;
        this.fn_answer_file = fn_answer_file;
        this.dx_created = dx_created;
        this.objectOperationType = objectOperationType;
        this.isDelete = isDelete;
        this.isSynchronization = isSynchronization;
        this.tid = tid;
        this.blockTid = blockTid;
    }

    @Generated(hash = 1542285449)
    public Feedbacks() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_imei() {
        return this.c_imei;
    }

    public void setC_imei(String c_imei) {
        this.c_imei = c_imei;
    }

    public long getFn_user() {
        return this.fn_user;
    }

    public void setFn_user(long fn_user) {
        this.fn_user = fn_user;
    }

    public long getFn_type() {
        return this.fn_type;
    }

    public void setFn_type(long fn_type) {
        this.fn_type = fn_type;
    }

    public String getC_question() {
        return this.c_question;
    }

    public void setC_question(String c_question) {
        this.c_question = c_question;
    }

    public String getD_date_question() {
        return this.d_date_question;
    }

    public void setD_date_question(String d_date_question) {
        this.d_date_question = d_date_question;
    }

    public String getFn_question_file() {
        return this.fn_question_file;
    }

    public void setFn_question_file(String fn_question_file) {
        this.fn_question_file = fn_question_file;
    }

    public String getC_answer() {
        return this.c_answer;
    }

    public void setC_answer(String c_answer) {
        this.c_answer = c_answer;
    }

    public String getD_date_answer() {
        return this.d_date_answer;
    }

    public void setD_date_answer(String d_date_answer) {
        this.d_date_answer = d_date_answer;
    }

    public String getFn_answer_file() {
        return this.fn_answer_file;
    }

    public void setFn_answer_file(String fn_answer_file) {
        this.fn_answer_file = fn_answer_file;
    }

    public String getDx_created() {
        return this.dx_created;
    }

    public void setDx_created(String dx_created) {
        this.dx_created = dx_created;
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

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 472964965)
    public Users getUser() {
        long __key = this.fn_user;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UsersDao targetDao = daoSession.getUsersDao();
            Users userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1934989217)
    public void setUser(@NotNull Users user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'fn_user' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            fn_user = user.getId();
            user__resolvedKey = fn_user;
        }
    }

    @Generated(hash = 506996655)
    private transient Long type__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1438009058)
    public FeedbackTypes getType() {
        long __key = this.fn_type;
        if (type__resolvedKey == null || !type__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FeedbackTypesDao targetDao = daoSession.getFeedbackTypesDao();
            FeedbackTypes typeNew = targetDao.load(__key);
            synchronized (this) {
                type = typeNew;
                type__resolvedKey = __key;
            }
        }
        return type;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 907180727)
    public void setType(@NotNull FeedbackTypes type) {
        if (type == null) {
            throw new DaoException(
                    "To-one property 'fn_type' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.type = type;
            fn_type = type.getId();
            type__resolvedKey = fn_type;
        }
    }

    @Generated(hash = 799969605)
    private transient String questionFile__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2109594965)
    public Files getQuestionFile() {
        String __key = this.fn_question_file;
        if (questionFile__resolvedKey == null
                || questionFile__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FilesDao targetDao = daoSession.getFilesDao();
            Files questionFileNew = targetDao.load(__key);
            synchronized (this) {
                questionFile = questionFileNew;
                questionFile__resolvedKey = __key;
            }
        }
        return questionFile;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 460896348)
    public void setQuestionFile(Files questionFile) {
        synchronized (this) {
            this.questionFile = questionFile;
            fn_question_file = questionFile == null ? null : questionFile.getId();
            questionFile__resolvedKey = fn_question_file;
        }
    }

    @Generated(hash = 1929248307)
    private transient String answerFile__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 264570882)
    public Files getAnswerFile() {
        String __key = this.fn_answer_file;
        if (answerFile__resolvedKey == null || answerFile__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FilesDao targetDao = daoSession.getFilesDao();
            Files answerFileNew = targetDao.load(__key);
            synchronized (this) {
                answerFile = answerFileNew;
                answerFile__resolvedKey = __key;
            }
        }
        return answerFile;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 712777338)
    public void setAnswerFile(Files answerFile) {
        synchronized (this) {
            this.answerFile = answerFile;
            fn_answer_file = answerFile == null ? null : answerFile.getId();
            answerFile__resolvedKey = fn_answer_file;
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
    @Generated(hash = 740346356)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFeedbacksDao() : null;
    }
}
