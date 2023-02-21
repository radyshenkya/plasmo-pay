package net.radyshenkya.plasmopay.plasmo_api;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PlasmoApi {
    public static final String API_URL = "https://rp.plo.su/api";
    public static String token = null;

    public static void setToken(String token) {
        PlasmoApi.token = token;
    }

    public static void assertTokenNotNull() {
        if (PlasmoApi.token == null || PlasmoApi.token == "") {
            throw new IllegalArgumentException("PlasmoApi.token must be set!");
        }
    }

    public static void newTransfer(String from, String to, int amount, String message) throws ApiCallException {
        HttpURLConnection connection = getConnection("/bank/transfer", "POST");

        try {
            connection.connect();
        } catch (Exception e) {
            throw new ApiCallException("Cannot connect to Plasmo server.", e);
        }

        // Plasmo Error: Status-code: 400. Error message: Мать хокаге перевернулась в
        // гробу и эта проверка добавилась автоматически
        if (from == to)
            throw new ApiCallException("Cannot transfer to the same card", null);

        // Составить JSON объект
        JsonObject requestJsonBody = new JsonObject();
        requestJsonBody.addProperty("from", from);
        requestJsonBody.addProperty("to", to);
        requestJsonBody.addProperty("amount", amount);
        requestJsonBody.addProperty("message", message);

        // Конвертировать его в строчку
        String requestBody = requestJsonBody.toString();

        // Отправить строку в connection
        try (OutputStream outStream = connection.getOutputStream()) {
            outStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
            outStream.close();
        } catch (Exception e) {
            throw new ApiCallException("Error while sending request", e);
        }

        // Получить ответ от connection'а
        String responseBody = null;

        try (InputStream inStream = connection.getInputStream()) {
            responseBody = new String(inStream.readAllBytes(), StandardCharsets.UTF_8);
            inStream.close();
        } catch (Exception e) {
            throw new ApiCallException("Error while reading server reponse", e);
        }

        // Конвертировать ответ в JsonObject
        JsonObject responseBodyJson = (new Gson()).fromJson(responseBody, JsonObject.class);

        // Проверить его на кайфовость
        assertPlasmoApiResponse(responseBodyJson);
    }

    protected static void assertPlasmoApiResponse(JsonObject response) throws ApiCallException {
        if (!response.get("status").getAsBoolean()) {
            throw new ApiCallException(response.get("error").getAsJsonObject().get("msg").getAsString(), null);
        }
    }

    protected static HttpURLConnection getConnection(String endpoint, String method) throws ApiCallException {
        try {

            URL url = new URL(API_URL.concat(endpoint));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            connection.setDoOutput(true);
            connection.setDoInput(true);

            if (method != "GET") {
                assertTokenNotNull();

                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Cookie", String.format("rp_token=%s", PlasmoApi.token));
            }

            return connection;
        } catch (Exception e) {
            throw new ApiCallException(e.getMessage(), e);
        }
    }
}
