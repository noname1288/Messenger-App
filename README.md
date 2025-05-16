// --------------- Auth & Profile ---------------
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val fcmToken: String = ""
)

// --------------- Messaging ---------------
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

data class ChatRoom(               // để hiển thị “recent chat”
    val chatId: String = "",
    val partnerId: String = "",
    val partnerName: String = "",
    val partnerAvatar: String = "",
    val lastMessage: String = "",
    val lastTimestamp: Long = 0
)


*******************************
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
             └─ {messageId} (DOCUMENT)  → schema = Message ở trên

inbox (COLLECTION)
  └─ {uid} (~userUID ; DOCUMENT placeholder)
        └─ rooms (SUB‑COLLECTION)
             └─ {chatId} (DOCUMENT)     → schema = ChatRoom


*******************************
USSE CASE (đã hoàn thanh)
| UC                    | Màn hình / ViewModel | Trạng thái nên dùng                                                   | `T` trong `Success<T>` nên là…            | Giải thích ngắn gọn                                   |
| --------------------- | -------------------- | --------------------------------------------------------------------- | ----------------------------------------- | ----------------------------------------------------- |
| 01 Đăng ký            | `AuthViewModel`      | `UIState<Unit>`                                                       | `Unit`                                    | Chỉ cần biết “OK” hay “Error”.                        |
| 02 Tạo/cập nhật hồ sơ | `ProfileViewModel`   | `UIState<User>`                                                       | `User` (domain model)                     | Trả ngay hồ sơ để vẽ lại avatar, tên…                 |
| 03 Đăng nhập          | `AuthViewModel`      | `UIState<FirebaseUser>` *(hoặc)* `UIState<Pair<FirebaseUser,String>>` | `FirebaseUser` **hoặc** `(user, idToken)` | Tuỳ bạn có cần token trên UI không.                   |
| 04 Tìm người          | `SearchViewModel`    | `UIState<List<User>>`                                                 | `List<User>`                              | Trả về list để hiển thị kết quả tìm kiếm.             |
| 05 Chat 1‑1 (Gửi)     | `ChatViewModel`      | `UIState<Unit>`                                                       | `Unit`                                    | Bấm “Send” → chỉ cần Loading / Error / Success(Unit). |
| 05 Chat 1‑1 (Nhận)    | `ChatViewModel`      | **Không dùng `UIState`** → `StateFlow<List<Message>>`                 | —                                         | Luồng real‑time, cứ push list mới.                    |
| 06 Inbox gần đây      | `InboxViewModel`     | **Không dùng `UIState`** → `StateFlow<List<Thread>>`                  | —                                         | Cũng là real‑time, lắng nghe thường trực.             |


