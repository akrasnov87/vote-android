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
import ru.mobnius.vote.data.manager.rpc.RPCResult;
import ru.mobnius.vote.data.manager.rpc.SingleItemQuery;
import ru.mobnius.vote.data.storage.models.Routes;
import ru.mobnius.vote.ui.model.BurndownItemModel;
import ru.mobnius.vote.utils.DateUtil;

public class BurndownChartAsyncTask extends AsyncTask<Long, Void, List<BurndownItemModel>> {

    private final OnBurnDownLoadedListener mListener;

    public BurndownChartAsyncTask(OnBurnDownLoadedListener listener) {
        mListener= listener;
    }

    @Override
    protected List<BurndownItemModel> doInBackground(Long... longs) {
        List<BurndownItemModel> items = new ArrayList<>();
        long count = longs[0];
        try {
            List<Routes> routes = DataManager.getInstance().getDaoSession().getRoutesDao().loadAll();
            if (routes.size() > 0) {
                RPCResult[] results = RequestManager.rpc(MobniusApplication.getBaseUrl(),
                        Authorization.getInstance().getUser().getCredentials().getToken(),
                        "cf_burndown",
                        "Select",
                        new SingleItemQuery(routes.get(0).f_type, Authorization.getInstance().getUser().getUserId()));
                if(results != null) {
                    if (results[0].isSuccess() && results[0].result.total > 0) {
                        for (JSONObject jsonObject : results[0].result.records) {
                            BurndownItemModel model = new BurndownItemModel();
                            model.d_date = DateUtil.convertStringToDate(jsonObject.getString("d_date"));
                            model.n_count = (count -= jsonObject.getLong("n_count"));
                            items.add(model);
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
    protected void onPostExecute(List<BurndownItemModel> items) {
        super.onPostExecute(items);

        mListener.onBurnDownLoaded(items);
    }

    public interface OnBurnDownLoadedListener {
        void onBurnDownLoaded(List<BurndownItemModel> items);
    }
}
