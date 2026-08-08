# ReBook Mobile Application

ReBook is a collaborative marketplace for students to buy, sell, borrow, and donate educational materials like books, lecture notes, past papers, and calculators.

## 🚀 Firebase Integration Checklist
The application is now fully connected to Firebase. Follow these steps to verify the connection:

### 1. User Registration & Roles
- [ ] **Test:** Register a new user and select **Seller** role.
- [ ] **Check:** Open [Firebase Console](https://console.firebase.google.com/) -> Realtime Database. Verify a new node exists under `users/{uid}` with `"role": "SELLER"` and `"status": "Pending"`.

### 2. Admin Approval Flow
- [ ] **Test:** Log in as an Admin. Go to the **Students** section.
- [ ] **Action:** Click **Verify** on the newly registered Seller.
- [ ] **Check:** In Firebase Console, ensure the user's status has changed to `"Verified"`.

### 3. Material Listing (Seller)
- [ ] **Test:** Log in as the Verified Seller. Go to **Add Material**.
- [ ] **Action:** Fill in details for a book (e.g., "Java Programming") and click **Publish**.
- [ ] **Check:** Verify the item appears in the `materials` node in Firebase.

### 4. Marketplace Browsing (Buyer)
- [ ] **Test:** Log in as a Buyer. Navigate to **Books -> Sales**.
- [ ] **Check:** Ensure the "Java Programming" book uploaded by the seller is visible in the list.

### 5. Transactions & Status Updates
- [ ] **Test:** As a Buyer, click **Get Item** on a book. Complete the purchase/request form.
- [ ] **Check 1:** A new entry should appear in the `transactions` node.
- [ ] **Check 2:** The book's status in the `materials` node should automatically change to `"Sold"` or `"Borrowed"`.
- [ ] **Check 3:** The book should no longer appear in the "Available" list for other buyers.

### 6. Real-time Communication
- [ ] **Test:** Use the **Chat** feature between two logged-in users.
- [ ] **Check:** Verify messages appear instantly in the `chats` node.

---

## 🛠 Database Structure
- `/users`: User profiles, roles (ADMIN/SELLER/BUYER), and verification status.
- `/materials`: All listed items categorized by type and category.
- `/transactions`: History of all marketplace activity.
- `/chats`: Real-time messaging threads.
- `/notifications`: Alerts for sales and approvals.
- `/feedbacks`: Seller ratings and community reviews.
