# 📱 Firebase Messaging App – Android Kotlin + Firebase Realtime Chat

Ứng dụng nhắn tin thời gian thực sử dụng Firebase, hỗ trợ đăng ký, đăng nhập, gửi và nhận tin nhắn, cập nhật cuộc trò chuyện gần nhất và tích hợp thông báo đẩy (FCM).

---

## 🧩 Data Models

### 🔐 1. Auth & Profile
```kotlin
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = ""
)
````

### 💬 2. Messaging

```kotlin
data class Message(
    val id: String = "",
    val chatId: String = "",          // uidA_uidB (sorted)
    val senderId: String = "",
    val receiverId: String = "",
    val text: String? = null,
    val photoUri: String? = null,
    val photoMimeType: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatRoom(
    val chatId: String = "",
    val partnerId: String = "",
    val partnerName: String = "",
    val partnerAvatar: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0
)
```

---

## 🗂️ Firestore Structure

```plaintext
users (COLLECTION)
 └─ {uid} (DOCUMENT)
     • email, displayName, avatarUrl, fcmToken, createdAt

chats (COLLECTION)
 └─ {chatId = uidSmall_uidBig} (DOCUMENT)
     • participants: [uidA, uidB]
     • lastMessage, lastTimestamp
     └─ messages (SUB-COLLECTION)
         └─ {messageId} → schema = Message

inbox (COLLECTION)
 └─ {uid} (DOCUMENT)
     └─ rooms (SUB-COLLECTION)
         └─ {chatId} → schema = ChatRoom
```

---

## ✅ Use Cases & ViewModels

| #  | Use Case            | ViewModel        | State Management                      | Trả về (`Success<T>`) | Ghi chú                                |
| -- | ------------------- | ---------------- | ------------------------------------- | --------------------- | -------------------------------------- |
| 01 | Đăng ký             | AuthViewModel    | UIState<Unit>                         | Unit                  | Chỉ cần biết thành công/thất bại       |
| 02 | Tạo/Cập nhật hồ sơ  | ProfileViewModel | UIState<User>                         | User                  | Trả về User để cập nhật UI             |
| 03 | Đăng nhập           | AuthViewModel    | UIState<FirebaseUser> hoặc Pair       | FirebaseUser / Pair   | Tuỳ thuộc vào UI có cần token          |
| 04 | Tìm người           | SearchViewModel  | UIState\<List<User>>                  | List<User>            | Hiển thị kết quả tìm kiếm              |
| 05 | Gửi tin nhắn        | ChatViewModel    | UIState<Unit>                         | Unit                  | Xác nhận gửi thành công                |
| 06 | Nhận tin nhắn       | ChatViewModel    | `StateFlow<List<Message>>` (Realtime) | —                     | Tự động cập nhật realtime              |
| 07 | Xem hộp thư (Inbox) | InboxViewModel   | `StateFlow<List<Thread>>` (Realtime)  | —                     | Lắng nghe các cuộc trò chuyện mới nhất |

---

## 🔧 Công nghệ sử dụng

* **Kotlin + Jetpack Compose**
* **Firebase Authentication**
* **Firebase Firestore (Realtime & Subcollection)**
* **Firebase Cloud Messaging (FCM)**
* **Cloud Functions for Push Notifications**
* **StateFlow + ViewModel + Clean MVVM Architecture**
* **Navigation Compose, Android Permissions, PendingIntent, Notification**

---

## 📦 Tính năng nổi bật

* Gửi và nhận tin nhắn realtime
* Thông báo đẩy khi có tin nhắn mới
* Tìm kiếm người dùng và tạo cuộc trò chuyện mới
* Cập nhật avatar, tên hiển thị và FCM token người dùng
* Giao diện Compose hiện đại, điều hướng đa màn hình

---

## 🧪 Cấu trúc Project (rút gọn)

```
├── core 
├── data
│   ├── remote/api, dto, repository
├── domain
│   ├── entity, repository, usecase, session
├── ui
│   ├── login, register, message, inbox, profile, ..., theme
├── service_locator
├── utils
├── navigation
```

---
