
# 📱 Firebase Messaging App – Data Models & Use Cases

## 1. 🔐 Auth & Profile

```kotlin
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = ""
)
````

## 2. 💬 Messaging

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

data class ChatRoom( // Hiển thị “recent chat”
    val chatId: String = "",
    val partnerId: String = "",
    val partnerName: String = "",
    val partnerAvatar: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0
)
```

---

## 3. 🧩 Firestore Structure

```
users (COLLECTION)
  └─ {uid} (DOCUMENT)
        • email
        • displayName
        • avatarUrl
        • fcmToken
        • createdAt

chats (COLLECTION)
  └─ {chatId = uidSmall_uidBig} (DOCUMENT)
        • participants: [uidA, uidB]
        • lastMessage: "👍"
        • lastTimestamp: 1714800000000
        └─ messages (SUB‑COLLECTION)
             └─ {messageId} (DOCUMENT)
                 → schema = Message

inbox (COLLECTION)
  └─ {uid} (DOCUMENT)
        └─ rooms (SUB‑COLLECTION)
             └─ {chatId} (DOCUMENT)
                 → schema = ChatRoom
```

---

## 4. ✅ Use Cases Overview

| Use Case                | ViewModel          | State Management                                                  | `T` in `Success<T>`                   | Description                               |
| ----------------------- | ------------------ | ----------------------------------------------------------------- | ------------------------------------- | ----------------------------------------- |
| 01 – Đăng ký            | `AuthViewModel`    | `UIState<Unit>`                                                   | `Unit`                                | Chỉ cần biết thành công/thất bại.         |
| 02 – Tạo/Cập nhật hồ sơ | `ProfileViewModel` | `UIState<User>`                                                   | `User`                                | Trả về `User` để cập nhật UI.             |
| 03 – Đăng nhập          | `AuthViewModel`    | `UIState<FirebaseUser>` hoặc `UIState<Pair<FirebaseUser,String>>` | `FirebaseUser` hoặc `(user, idToken)` | Tuỳ vào việc UI có cần token hay không.   |
| 04 – Tìm người          | `SearchViewModel`  | `UIState<List<User>>`                                             | `List<User>`                          | Hiển thị kết quả tìm kiếm.                |
| 05 – Gửi tin nhắn       | `ChatViewModel`    | `UIState<Unit>`                                                   | `Unit`                                | Chỉ cần biết gửi thành công hay chưa.     |
| 05 – Nhận tin nhắn      | `ChatViewModel`    | `StateFlow<List<Message>>` *(realtime)*                           | —                                     | Dùng luồng realtime để cập nhật liên tục. |
| 06 – Inbox gần đây      | `InboxViewModel`   | `StateFlow<List<Thread>>` *(realtime)*                            | —                                     | Lắng nghe các cuộc trò chuyện mới nhất.   |


