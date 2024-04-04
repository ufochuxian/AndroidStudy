package com.eric.routers.apt;
import android.app.Activity;
import java.util.Map;
import com.eric.routers.IRouteLoad;
import com.eric.routers.apt.SecondActivity;
public class CustomApt_router implements IRouteLoad {
@Override
public void onLoad(Map<String, Class<? extends Activity>> routes) {
routes.put("/food/main",SecondActivity.class);
}
}