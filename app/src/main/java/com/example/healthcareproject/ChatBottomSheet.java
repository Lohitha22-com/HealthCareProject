//package com.example.healthcareproject;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.Request;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ChatBottomSheet extends BottomSheetDialogFragment {
//
//    private static final String API_KEY = "API_KEY";
//    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=" + API_KEY;
//    RecyclerView recyclerView;
//    EditText edtMessage;
//    ImageButton btnSend;
//    MessageAdapter adapter;
//    List<Message> messageList = new ArrayList<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.chat_activity, container, false);
//
//        recyclerView = view.findViewById(R.id.recyclerChat);
//        edtMessage = view.findViewById(R.id.editMessage);
//        btnSend = view.findViewById(R.id.sendBtn);
//
//        adapter = new MessageAdapter(messageList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//        btnSend.setOnClickListener(v -> sendMessage());
//        return view;
//    }
//    private void sendMessage() {
//        String userText = edtMessage.getText().toString().trim();
//        if (userText.isEmpty()) return;
//
//        adapter.addMessage(new Message(userText, Message.SENT_BY_USER));
//        edtMessage.setText("");
//        callGemini(userText);
//    }
//
//    private void callGemini(String userText){
//        try {
//            JSONObject part = new JSONObject();
//            part.put("text", userText);
//
//            JSONArray parts = new JSONArray();
//            parts.put(part);
//
//            JSONObject content = new JSONObject();
//            content.put("parts", parts);
//
//            JSONArray contents = new JSONArray();
//            contents.put(content);
//
//            JSONObject body = new JSONObject();
//            body.put("contents", contents);
//
//            JsonObjectRequest request = new JsonObjectRequest(
//                    Request.Method.POST, API_URL, body,
//                    response -> {
//                        try{
//                            String reply = response
//                                    .getJSONArray("candidates")
//                                    .getJSONObject(0)
//                                    .getJSONObject("content")
//                                    .getJSONArray("parts")
//                                    .getJSONObject(0)
//                                    .getString("text");
//                            adapter.addMessage(new Message(reply, Message.SENT_BY_BOT));
//                            recyclerView.scrollToPosition(messageList.size() -1);
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    },
//                    volleyError -> {
//                        String responseBody = "no response body";
//                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
//                            responseBody = new String(volleyError.networkResponse.data, StandardCharsets.UTF_8);
//                        }
//                        android.util.Log.e("GeminiAPIError", "Status: " +(volleyError.networkResponse != null ? volleyError.networkResponse.statusCode : "null") + "Body: " + body);
//                    }
//            );
//            Volley.newRequestQueue(getContext()).add(request);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
