package com.example.messengerapp.navigation

import androidx.navigation.NavController


/**
 * Điều hướng chuẩn có xử lý:
 * - Không trùng route (launchSingleTop)
 * - Quay lại đúng màn hình gốc (popUpTo)
 * - Giữ lại state (restoreState)
 */
fun NavController.safeNavigate(
    route: String,
    popUpToRoute: String = AppRoute.HOME,
    isInclusive: Boolean = false,
    restore: Boolean = true
) {
    this.navigate(route) {
        popUpTo(popUpToRoute) {
            inclusive = isInclusive
            saveState = restore
        }
        launchSingleTop = true
        restoreState = restore
    }
}

fun NavController.popBackIfCan() {
    // Nếu có thể pop ra khỏi stack thì tiến hành pop
    if (popBackStack()) {
        // Có thể xử lý thêm nếu cần, ví dụ log hoặc thực hiện một hành động sau pop
    }
}

fun NavController.navigateWithArgs(
    route: String,
    vararg args: Any,
    popUpToRoute: String = AppRoute.HOME,
    isInclusive: Boolean = false,
    restore: Boolean = true
) {
    // Giả sử route có định dạng như "detail/%s" và args sẽ thay thế %s
    val formattedRoute = String.format(route, *args)
    this.navigate(formattedRoute) {
        popUpTo(popUpToRoute) {
            inclusive = isInclusive
            saveState = restore
        }
        launchSingleTop = true
        restoreState = restore
    }
}


fun NavController.navigateOnce(
    route: String,
    popUpToRoute: String = AppRoute.HOME,
    isInclusive: Boolean = false,
    restore: Boolean = true
) {
    // Kiểm tra nếu route hiện tại khác với route cần điều hướng
    if (currentBackStackEntry?.destination?.route != route) {
        // Sử dụng hàm safeNavigate đã định nghĩa từ trước để đảm bảo cấu hình phù hợp
        safeNavigate(route, popUpToRoute, isInclusive, restore)
    }
}