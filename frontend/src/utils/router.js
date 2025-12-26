/**
 * 路由工具函数
 * 用于安全地处理路由导航，避免重复导航错误
 */

/**
 * 安全的路由push方法
 * @param {VueRouter} router - Vue Router实例
 * @param {string} path - 目标路径
 */
export function safePush(router, path) {
  if (router.currentRoute.path !== path) {
    return router.push(path).catch(err => {
      // 忽略重复导航错误
      if (err.name !== 'NavigationDuplicated') {
        console.error('路由导航错误:', err)
        throw err
      }
    })
  }
  return Promise.resolve()
}

/**
 * 安全的路由replace方法
 * @param {VueRouter} router - Vue Router实例
 * @param {string} path - 目标路径
 */
export function safeReplace(router, path) {
  if (router.currentRoute.path !== path) {
    return router.replace(path).catch(err => {
      // 忽略重复导航错误
      if (err.name !== 'NavigationDuplicated') {
        console.error('路由导航错误:', err)
        throw err
      }
    })
  }
  return Promise.resolve()
}

