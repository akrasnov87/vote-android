package ru.mobnius.vote.utils;

import java.util.HashMap;

interface IAdapterItemPosition {
    boolean equals(HashMap<String, Object> map, Object id);
}