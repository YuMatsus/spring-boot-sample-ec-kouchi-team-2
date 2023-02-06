package com.example.springbootsampleec.services;

import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;

public interface GeoApiService {
    // 住所から座標を取得
    LatLng getLatLng(String address);
    // 座標間の距離情報を取得
    DirectionsResult getDirectionsResult(LatLng originLatLng, LatLng destinationLatLng);
    // 複数の座標間の距離情報を取得
    DistanceMatrix getDistanceMatrix(LatLng[] originLatLngs, LatLng[] destinationLatLngs);
    // 店舗-ユーザー間の所要時間(秒)を取得
    long getRequiredTimeStoreToUser(Store store, User user);
}
