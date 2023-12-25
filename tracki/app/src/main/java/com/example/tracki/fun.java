package com.example.tracki;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class fun {
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String uid=mAuth.getUid();

    public static void sendMessage(String message, String sender, String age, String gender, String mainNode, Uri image){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        String pushId=reference.child(mainNode).push().getKey();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getUid();

        if (image!=null ){

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(pushId);

            storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            Map<String,String> msg=new HashMap<>();
                            msg.put("message",message);
                            msg.put("sender",sender);
                            msg.put("age",age);
                            msg.put("gender",gender);
                            msg.put("imageUrl",imageUrl);
                            msg.put("uid",uid);
                            reference.child(mainNode).child(pushId).setValue(msg);
                        }
                    });
                }
            });
        }

        else {
            Map<String,String> msg=new HashMap<>();
            msg.put("message",message);
            msg.put("sender",sender);
            msg.put("age",age);
            msg.put("gender",gender);
            msg.put("uid",uid);
            reference.child(mainNode).child(pushId).setValue(msg);

        }
    }

    public static void sendMessage1(String message, String sender, String age, String gender, String mainNode, Uri image,String roomId){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        String pushId=reference.child(mainNode).child(roomId).push().getKey();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getUid();

        if (image!=null ){

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(pushId);

            storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            Map<String,String> msg=new HashMap<>();
                            msg.put("message",message);
                            msg.put("sender",sender);
                            msg.put("age",age);
                            msg.put("gender",gender);
                            msg.put("imageUrl",imageUrl);
                            msg.put("uid",uid);
                            reference.child(mainNode).child(roomId).child(pushId).setValue(msg);
                        }
                    });
                }
            });
        }

        else {
            Map<String,String> msg=new HashMap<>();
            msg.put("message",message);
            msg.put("sender",sender);
            msg.put("age",age);
            msg.put("gender",gender);
            msg.put("uid",uid);
            reference.child(mainNode).child(roomId).child(pushId).setValue(msg);

        }
    }



    public void sendMessageChat(String message, String sender, String age, String gender, Uri image,String receiver,String rGender,String msgId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        if (msgId==null){
            msgId=reference.child("chat").push().getKey();
        }

        String pushId=reference.child("chat").child(msgId).push().getKey();


        if (image!=null ){

            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(pushId);

            String finalMsgId = msgId;
            storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            Map<String,String> msg=new HashMap<>();
                            msg.put("message",message);
                            msg.put("sender",sender);
                            msg.put("age",age);
                            msg.put("gender",gender);
                            msg.put("imageUrl",imageUrl);
                            msg.put("uid",uid);
                            Map<String,String>send=new HashMap();
                            Map<String,String> rec=new HashMap<>();
                            send.put("gender",rGender);
                            send.put("msgId", finalMsgId);
                            rec.put("gender",rGender);
                            rec.put("msgId",finalMsgId);
                            reference.child("active").child(sender).child("messages").child(receiver).setValue(rec);
                            reference.child("active").child(receiver).child("messages").child(sender).setValue(send);
                            reference.child("chat").child(finalMsgId).child(pushId).setValue(msg);

                        }
                    });
                }
            });
        }

        else {
            String finalMsgId = msgId;
            Map<String,String> msg=new HashMap<>();
            msg.put("message",message);
            msg.put("sender",sender);
            msg.put("age",age);
            msg.put("gender",gender);
            msg.put("uid",uid);
            Map<String,String>send=new HashMap();
            Map<String,String> rec=new HashMap<>();
            send.put("gender",rGender);
            send.put("msgId", finalMsgId);
            rec.put("gender",rGender);
            rec.put("msgId",finalMsgId);
            reference.child("active").child(sender).child("messages").child(receiver).setValue(rec);
            reference.child("active").child(receiver).child("messages").child(sender).setValue(send);
            reference.child("chat").child(finalMsgId).child(pushId).setValue(msg);
        }
    }

}
