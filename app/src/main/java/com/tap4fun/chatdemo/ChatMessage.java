package com.tap4fun.chatdemo;

public class ChatMessage {
    enum ChatType {
        P2P_CHAT,
        P2G_CHAT,
    }

    enum MsgBodyType {
        MESSAGE_BODY_TEXT,
        MESSAGE_BODY_AUDIO,
        MESSAGE_BODY_PICTURE,
        MESSAGE_BODY_UNKNOWN,
    }

    enum MsgType {
        MSG_RECEIVED,
        MSG_SEND
    }

    private MsgType msgType;
    private ChatType chatType;
    private MsgBodyType msgBodyType;
    private int clientMsgId;
    private String from;
    private String to;
    private String sessionId;
    private String body;
    private String userData;
    private String sendTime;
    private String recvTime;
    private String serverTimestamp;
    private boolean isOffline;
    private boolean isAlreadyRead;
    private boolean errError;
    private String errCode;
    private String errDesc;
    private String errSubdesc;
    private String errMutedExpire;
    private String subject;

    public MsgType getMsgType() {
        return msgType;
    }
    public void setMsgType(MsgType type) { msgType = type; }

    public ChatType getChatType() {
        return chatType;
    }
    public void setChatType(ChatType type) { chatType = type; }

    public MsgBodyType getMsgBodyType() {
        return msgBodyType;
    }
    public void setMsgBodyType(MsgBodyType type) { msgBodyType = type; }

    public int getClientMsgId() {
        return clientMsgId;
    }
    public void setClientMsgId(int id) { clientMsgId = id; }

    public String getFrom() {
        return from;
    }
    public void setFrom(String f) { from = f;}

    public String getTo() {
        return to;
    }
    public void setTo(String t) { to = t; }

    public String getBody() {
        return body;
    }
    public void setBody(String b) { body = b; }

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sid) { sessionId = sid; }

    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String stime) { sendTime = stime; }

    public String getRecvTime() {
        return recvTime;
    }
    public void setRecvTime(String rtime) { recvTime = rtime; }

    public String getServerTimestamp() {
        return serverTimestamp;
    }
    public void setServerTimestamp(String time) { serverTimestamp = time; }

    public String getUserData() {
        return userData;
    }
    public void setUserData(String udata) { userData = udata; }

    public String getErrCode() {
        return errCode;
    }
    public void setErrCode(String err) { errCode = err; }

    public String getErrDesc() {
        return errDesc;
    }
    public void setErrDesc(String err) { errDesc = err; }

    public String getErrSubdesc() {
        return errSubdesc;
    }
    public void setErrSubdesc(String err) { errSubdesc = err; }

    public String getErrMutedExpire() {
        return errMutedExpire;
    }
    public void setErrMutedExpire(String exp) { errMutedExpire = exp; }

    public boolean isAlreadyRead() {
        return isAlreadyRead;
    }
    public void setAlreadyRead(boolean alreadyRead) {
        isAlreadyRead = alreadyRead;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public void setErrError(boolean errError) {
        this.errError = errError;
    }

    public boolean isErrError() {
        return errError;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
