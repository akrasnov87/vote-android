package ru.mobnius.vote.ui.data;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mobnius.vote.data.Logger;
import ru.mobnius.vote.data.manager.DataManager;
import ru.mobnius.vote.data.manager.MobniusApplication;
import ru.mobnius.vote.data.manager.RequestManager;
import ru.mobnius.vote.data.manager.authorization.Authorization;
import ru.mobnius.vote.data.manager.rpc.FilterItem;
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.ui.model.RatingItemModel;

public class RatingAsyncTask extends AsyncTask<Integer, Void, List<RatingItemModel>> {

    private final OnRatingLoadedListener mListener;

    public RatingAsyncTask(OnRatingLoadedListener listener) {
        mListener = listener;
    }

    @Override
    protected List<RatingItemModel> doInBackground(Integer... integers) {
        List<RatingItemModel> items = new ArrayList<>();
        try {
            List<Routes> routes = DataManager.getInstance().getDaoSession().getRoutesDao().loadAll();
            if (routes.size() > 0) {

                SingleItemQuery query = new SingleItemQuery(routes.get(0).f_type);
                if(integers.length > 0 && integers[0] != null) {
                    FilterItem filterItem = new FilterItem("n_uik", integers[0]);
                    Object[] filters = new Object[1];
                    filters[0] = filterItem;
                    query.setFilter(filters);
                }

                RPCResult[] results = RequestManager.rpc(MobniusApplication.getBaseUrl(),
                        Authorization.getInstance().getUser().getCredentials().getToken(),
                        "cf_rating",
                        "Select",
                        query);
                if(results != null) {
                    if (results[0].isSuccess() && results[0].result.total > 0) {
                        int i = 1;
                        for (JSONObject jsonObject : results[0].result.records) {
                            RatingItemModel model = new RatingItemModel();
                            model.id = i;
                            model.user_id = jsonObject.getInt("user_id");
                            model.n_uik = jsonObject.getInt("n_uik");
                            model.c_login = jsonObject.getString("c_login");
                            model.n_all = jsonObject.getInt("n_all");
                            model.n_count = jsonObject.getInt("n_count");
                            model.n_today_count = jsonObject.getInt("n_today_count");

                            items.add(model);
                            i++;
                        }
                    } else{
                        if (!results[0].isSuccess()) {
                            Logger.error(new Exception(Objects.requireNonNull(results)[0].meta.msg));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return items;
    }

    @Override
    protected void onPostExecute(List<RatingItemModel> items) {
        super.onPostExecute(items);

        mListener.onRatingLoaded(items);
    }

    public interface OnRatingLoadedListener {
        void onRatingLoaded(List<RatingItemModel> items);
    }
}
