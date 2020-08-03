package ru.mobnius.vote.data.storage.models;

import com.google.gson.annotations.Expose;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@SuppressWarnings("unused")
@Entity(nameInDb = "cs_answer")
public class Answer {
    /**
     * Идентификатор
     */
    @Id
    @Property(nameInDb = "id")
    public Long id;

    /**
     * версия
     */
    @Expose
    public String c_text;

    /**
     * Описание изменения
     */
    @Expose
    public long f_question;

    /**
     * Вышестоящее отделение
     */
    @ToOne(joinProperty = "f_question")
    private Question question;

    /**
     * имя приложения
     */
    @Expose
    public long f_next_question;

    /**
     * Вышестоящее отделение
     */
    @ToOne(joinProperty = "f_next_question")
    private Question next_question;

    /**
     * скрыт
     */
    @Expose
    public String c_action;

    @Expose
    public int n_order;

    @Expose
    public String c_color;

    public String c_role;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1299531889)
    private transient AnswerDao myDao;

    @Generated(hash = 1748204030)
    public Answer(Long id, String c_text, long f_question, long f_next_question,
            String c_action, int n_order, String c_color, String c_role) {
        this.id = id;
        this.c_text = c_text;
        this.f_question = f_question;
        this.f_next_question = f_next_question;
        this.c_action = c_action;
        this.n_order = n_order;
        this.c_color = c_color;
        this.c_role = c_role;
    }

    @Generated(hash = 53889439)
    public Answer() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_text() {
        return this.c_text;
    }

    public void setC_text(String c_text) {
        this.c_text = c_text;
    }

    public long getF_question() {
        return this.f_question;
    }

    public void setF_question(long f_question) {
        this.f_question = f_question;
    }

    public long getF_next_question() {
        return this.f_next_question;
    }

    public void setF_next_question(long f_next_question) {
        this.f_next_question = f_next_question;
    }

    public String getC_action() {
        return this.c_action;
    }

    public void setC_action(String c_action) {
        this.c_action = c_action;
    }

    public int getN_order() {
        return this.n_order;
    }

    public void setN_order(int n_order) {
        this.n_order = n_order;
    }

    public String getC_color() {
        return this.c_color;
    }

    public void setC_color(String c_color) {
        this.c_color = c_color;
    }

    public String getC_role() {
        return this.c_role;
    }

    public void setC_role(String c_role) {
        this.c_role = c_role;
    }

    @Generated(hash = 527827701)
    private transient Long question__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 416516433)
    public Question getQuestion() {
        long __key = this.f_question;
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
    @Generated(hash = 611112861)
    public void setQuestion(@NotNull Question question) {
        if (question == null) {
            throw new DaoException(
                    "To-one property 'f_question' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.question = question;
            f_question = question.getId();
            question__resolvedKey = f_question;
        }
    }

    @Generated(hash = 1905550005)
    private transient Long next_question__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 848578357)
    public Question getNext_question() {
        long __key = this.f_next_question;
        if (next_question__resolvedKey == null
                || !next_question__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QuestionDao targetDao = daoSession.getQuestionDao();
            Question next_questionNew = targetDao.load(__key);
            synchronized (this) {
                next_question = next_questionNew;
                next_question__resolvedKey = __key;
            }
        }
        return next_question;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1601963259)
    public void setNext_question(@NotNull Question next_question) {
        if (next_question == null) {
            throw new DaoException(
                    "To-one property 'f_next_question' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.next_question = next_question;
            f_next_question = next_question.getId();
            next_question__resolvedKey = f_next_question;
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
    @Generated(hash = 1793985470)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerDao() : null;
    }
}
