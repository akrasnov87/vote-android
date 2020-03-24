package ru.mobnius.vote.ui.data;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mobnius.vote.data.manager.FilterManager;
import ru.mobnius.vote.data.manager.configuration.ConfigurationSetting;
import ru.mobnius.vote.ui.model.FilterItem;
import ru.mobnius.vote.ui.model.RouteItem;
import ru.mobnius.vote.utils.DateUtil;

/**
 * Служебный класс для фильтрации данных по маршрутам
 */
public class RouteFilterManager extends FilterManager<RouteItem> {
    public RouteFilterManager(String key) {
        super(key);
    }

    public RouteFilterManager(String key, String deSerialize) {
        super(key, deSerialize);
    }

    @Override
    public RouteItem[] toFilters(RouteItem[] items) {
        List<RouteItem> results = new ArrayList<>();
        for (RouteItem routeItem : items) {
            if (isAppend(routeItem, new IFilterCallback<RouteItem>() {
                /**
                 * Переопределяем поведение фильтра для даты
                 * @param filterItem фильтр
                 * @param item запись
                 * @param appendStatus результат добавления
                 * @return
                 */
                @Override
                public boolean onFilter(FilterItem filterItem, RouteItem item, boolean appendStatus) {
                    if (filterItem.getType().equals(ConfigurationSetting.DATE)) {
                        try {
                            Date filterDate = DateUtil.convertStringToDate(filterItem.getValue().replace("t", "T"));

                            if (item.dateStart.getTime() <= filterDate.getTime() &&
                                    filterDate.getTime() <= item.dateEnd.getTime()) {
                                return true;
                            }

                        } catch (ParseException ignore) {
                            appendStatus = false;
                        }
                    }
                    return appendStatus;
                }
            })) {
                results.add(routeItem);
            }
        }
        return results.toArray(new RouteItem[0]);
    }
}
