package com.zeowls.gifts.BackEndOwl;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.zeowls.gifts.provider.Contract.ShopEntry;
import com.zeowls.gifts.provider.Contract.ItemEntry;
import com.zeowls.gifts.provider.Contract.CartEntry;

public class Core {

    Context context;


    public Core(Context context) {
        this.context = context;
    }

    private String Domain = "http://bubble-zeowls-stage.herokuapp.com";

    private String getRequest(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(url);
        Log.d("url", url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        return data;
    }

    private String postRequest(String url, JSONObject params) throws IOException {
        URL url1 = new URL(Domain + url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestProperty("Authorization", "627562626c6520617069206b6579");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        DataOutputStream writer = new DataOutputStream(stream);

        Log.d("WARN", params.toString());
        // The LogCat prints out data like:
        // ID:test,Email:test@gmail.com,Pwd:test
        writer.writeBytes(params.toString());
        writer.flush();
        writer.close();
        stream.close();

        String data;
        BufferedReader reader;
        InputStream inputStream = connection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        Log.d("data", data);
        return data;
    }

    public JSONObject getShopItems(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetShopItems/" + id + "/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
                putItemsDB(json);
            } else {
                Log.d("getShopItems", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getShop(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetShop/" + id + "/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
                putShopsDB(json);
            } else {
                Log.d("getShopItems", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getItem(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetItem/" + id + "/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
                putItemsDB(json);
            } else {
                Log.d("get Items", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getAllShops() {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetAllShops/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
                putShopsDB(json);
            } else {
                Log.d("getAllShops", response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getSubCategoriesByCatID(int id) {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetSubCategoriesById/" + id + "/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
            } else {
                Log.d("getAllShops", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }


//    public JSONObject getUserOrdersByUserID(int id) {
//        JSONObject json = null;
//        try {
//            String response = getRequest(Domain + "/getOrdersByUser/" + id + "/JSON");
//            if (!response.equals("0")) {
//                json = new JSONObject(response);
//            } else {
//                Log.d("getOrdersByUser", response);
//            }
//        } catch (Exception e) {
////            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
////        putMoviesDB(json);
//        return json;
//    }


//    public JSONObject getSubAllCategories() {
//        JSONObject json = null;
//        try {
//            String response = getRequest(Domain + "/GetSubCategories/JSON");
//            if (!response.equals("0")) {
//                json = new JSONObject(response);
//            } else {
//                Log.d("GetSubCategories", response);
//            }
//        } catch (Exception e) {
////            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
////        putMoviesDB(json);
//        return json;
//    }

    public JSONObject getAllCategories() {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetCategories/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
            } else {
                Log.d("getAllShops", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getItemsByCategoryId(int id) {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetItemByCategory/" + id + "/JSON");
            if (!response.equals("0")) {
                json = new JSONObject(response);
                putItemsDB(json);
            } else {
                Log.d("get Items By Cat id", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONArray getHomePage() {
        JSONArray json = null;
        try {
            String response = getRequest(Domain + "/HomePage/JSON");
            if (!response.equals("0")) {
                json = new JSONArray(response);
                putItemsDB(json.getJSONObject(0));
            } else {
                Log.d("get Items By Cat id", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }


    public int getCredentials(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            String response = postRequest("/login", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public int signUpFBUser(JSONObject json, String fb_token) throws JSONException {
        int result = 0;
        try {
            json.put("fb_token", fb_token);
            String response = postRequest("/FBlogin", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public int signUpUser(String username, String password, String mobile) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            json.put("mobile", mobile);
            String response = postRequest("/signup", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public void addToCart(int shop_id, int item_id, String item_name, String item_price,
                          String item_image, String item_desc, String shop_name) {

        ContentValues cartValues = new ContentValues();
        Vector<ContentValues> cVVector = new Vector<>();

        cartValues.put(CartEntry.COLUMN_ITEM_ID, item_id);
        cartValues.put(CartEntry.COLUMN_ITEM_NAME, item_name);
        cartValues.put(CartEntry.COLUMN_ITEM_PRICE, item_price);
        cartValues.put(CartEntry.COLUMN_ITEM_PHOTO, item_image);
        cartValues.put(CartEntry.COLUMN_ITEM_DESC, item_desc);
        cartValues.put(CartEntry.COLUMN_SHOP_ID, shop_id);
        cartValues.put(CartEntry.COLUMN_SHOP_NAME, shop_name);

        cVVector.add(cartValues);

        int inserted = context.getContentResolver().update(CartEntry.CONTENT_URI, cartValues, CartEntry.COLUMN_ITEM_ID + " = ?", new String[]{String.valueOf(item_id)});
        Log.i(Core.class.getSimpleName(), "Cart Updatin8g Complete. " + inserted + " Inserted");
        if (inserted == 0) {
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = context.getContentResolver().bulkInsert(CartEntry.CONTENT_URI, cvArray);
                Log.i(Core.class.getSimpleName(), "Cart Inserting Complete. " + inserted + " Inserted");
            }
        }
    }

//    public int cartCount(int userId) {
//        int count = 0;
//        return count;
//    }

    public JSONObject getUserCart(int userId) {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/getUserShopCart/" + userId + "/");
            if (!response.equals("0")) {
                json = new JSONObject(response);
            } else {
                Log.d("addToShopCart", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return json;
    }

    public void putShopsDB(JSONObject jsonObject) {

        final String shop_list = "Shop";
        final String shop_id = "id";
        final String shop_name = "name";
        final String shop_owner_name = "owner_name";
        final String shop_email = "owner_email";
        final String shop_mobile = "mobile";
        final String shop_image = "profile_pic";
        final String shop_poster = "cover_pic";
        final String shop_description = "description";
        final String shop_short_description = "short_description";
        final String shop_address = "shop_address";
        try {

            JSONArray movies = jsonObject.getJSONArray(shop_list);
            Vector<ContentValues> cVVector = new Vector<>(movies.length());

            for (int i = 0; i < movies.length(); i++) {
                String id, title, owner_name, email, mobile, image, poster, description, short_description, fav, address;
                JSONObject movie = movies.getJSONObject(i);
                id = movie.getString(shop_id);
                title = movie.getString(shop_name);
                owner_name = movie.getString(shop_owner_name);
                email = movie.getString(shop_email);
                mobile = movie.getString(shop_mobile);
                image = movie.getString(shop_image);
                poster = movie.getString(shop_poster);
                description = movie.getString(shop_description);
                short_description = movie.getString(shop_short_description);
                address = movie.getString(shop_address);
                fav = "0";

                ContentValues moviesValues = new ContentValues();

                moviesValues.put(ShopEntry.COLUMN_ID, id);
                moviesValues.put(ShopEntry.COLUMN_FAV, fav);
                moviesValues.put(ShopEntry.COLUMN_NAME, title);
                moviesValues.put(ShopEntry.COLUMN_OWNER_NAME, owner_name);
                moviesValues.put(ShopEntry.COLUMN_EMAIL, email);
                moviesValues.put(ShopEntry.COLUMN_MOBILE, mobile);
                moviesValues.put(ShopEntry.COLUMN_PROFILE_PIC, image);
                moviesValues.put(ShopEntry.COLUMN_COVER_PIC, poster);
                moviesValues.put(ShopEntry.COLUMN_DESCRIPTION, description);
                moviesValues.put(ShopEntry.COLUMN_SHORT_DESCRIPTION, short_description);
                moviesValues.put(ShopEntry.COLUMN_ADDRESS, address);

                cVVector.add(moviesValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = context.getContentResolver().bulkInsert(ShopEntry.CONTENT_URI, cvArray);
            }

            Log.i(Core.class.getSimpleName(), "Shops adding Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void putItemsDB(JSONObject jsonObject) {

        final String item_list = "Items";
        final String item_id = "id";
        final String item_name = "name";
        final String item_image = "image";
        final String item_price = "price";
        final String item_description = "description";
        final String item_shop_id = "shop_id";
        final String item_cat_id = "sub_cat_id";
        try {

            JSONArray movies = jsonObject.getJSONArray(item_list);
            Vector<ContentValues> cVVector = new Vector<>(movies.length());

            for (int i = 0; i < movies.length(); i++) {
                String id, title, image, price, description, fav;
                int shop_id, cat_id;
                JSONObject movie = movies.getJSONObject(i);
                id = movie.getString(item_id);
                title = movie.getString(item_name);
                image = movie.getString(item_image);
                price = movie.getString(item_price);
                description = movie.getString(item_description);
                shop_id = movie.getInt(item_shop_id);
                cat_id = movie.getInt(item_cat_id);
                fav = "0";

                ContentValues moviesValues = new ContentValues();

                moviesValues.put(ItemEntry.COLUMN_ID, id);
                moviesValues.put(ItemEntry.COLUMN_FAV, fav);
                moviesValues.put(ItemEntry.COLUMN_NAME, title);
                moviesValues.put(ItemEntry.COLUMN_PRICE, price);
                moviesValues.put(ItemEntry.COLUMN_IMAGE, image);
                moviesValues.put(ItemEntry.COLUMN_DESCRIPTION, description);
                moviesValues.put(ItemEntry.COLUMN_SHOP_ID, shop_id);
                moviesValues.put(ItemEntry.COLUMN_CAT_ID, cat_id);

                cVVector.add(moviesValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = context.getContentResolver().bulkInsert(ItemEntry.CONTENT_URI, cvArray);
            }

            Log.i(Core.class.getSimpleName(), "Items adding Complete. " + inserted + " Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public int updateItemFavoriteDB(int fav, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemEntry.COLUMN_FAV, fav);
        return context.getContentResolver().update(ItemEntry.CONTENT_URI, contentValues, ItemEntry.COLUMN_ID + "=" + id, null);
    }


    public int updateShopFavoriteDB(int fav, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ShopEntry.COLUMN_FAV, fav);
        return context.getContentResolver().update(ShopEntry.CONTENT_URI, contentValues, ShopEntry.COLUMN_ID + "=" + id, null);
    }


    public int makeOrder(int item_id, int user_id, int quantity, String shipping_address) {

        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("item_id", item_id);
            json.put("user_id", user_id);
            json.put("quantity", quantity);
            json.put("shipping_address", shipping_address);
            String response = postRequest("/makeOrder", json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;

    }

    public JSONObject getUserOrders(int user_id) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();

        try {
            json.put("user_id", user_id);
            responseJson = new JSONObject(postRequest("/getOrdersByUser", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;
    }

    public JSONObject editUserOrder(int order_id, String shipping_address) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();

        try {
            json.put("order_id", order_id);
            json.put("shipping_address", shipping_address);
            responseJson = new JSONObject(postRequest("/editOrdersByUser", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;
    }

    public JSONObject registerDevice(int user_id, String token) {
        JSONObject json = new JSONObject();
        JSONObject responseJson = new JSONObject();
        try {
            json.put("user_id", user_id);
            json.put("device_token", token);
            responseJson = new JSONObject(postRequest("/registerDevice", json));
            Log.d("registerDeviceToken", token + "User:" + user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJson;

    }
}
