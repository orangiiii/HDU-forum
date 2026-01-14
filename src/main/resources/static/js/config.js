// API配置
const API_BASE_URL = 'http://localhost:8080';

const API = {
    // 用户相关
    USER_REGISTER: `${API_BASE_URL}/user/register`,
    USER_LOGIN: `${API_BASE_URL}/user/login`,
    USER_INFO: `${API_BASE_URL}/user/info`,
    USER_UPDATE: `${API_BASE_URL}/user/update`,
    
    // 分类相关
    CATEGORY_LIST: `${API_BASE_URL}/category/list`,
    CATEGORY_DETAIL: (id) => `${API_BASE_URL}/category/${id}`,
    
    // 帖子相关
    POST_LIST: `${API_BASE_URL}/post/list`,
    POST_DETAIL: (id) => `${API_BASE_URL}/post/${id}`,
    POST_BY_CATEGORY: (categoryId) => `${API_BASE_URL}/post/category/${categoryId}`,
    POST_BY_USER: (userId) => `${API_BASE_URL}/post/user/${userId}`,
    POST_CREATE: `${API_BASE_URL}/post/create`,
    POST_PUBLISH: `${API_BASE_URL}/post/publish`,
    POST_SAVE_DRAFT: `${API_BASE_URL}/post/saveDraft`,
    POST_DRAFT_LATEST: `${API_BASE_URL}/post/draft/latest`,
    POST_UPDATE: `${API_BASE_URL}/post/update`,
    POST_DELETE: (id) => `${API_BASE_URL}/post/${id}`,
    POST_LIKE: (id) => `${API_BASE_URL}/post/like/${id}`,
    
    // 评论相关
    COMMENT_BY_POST: (postId) => `${API_BASE_URL}/comment/post/${postId}`,
    COMMENT_BY_USER: (userId) => `${API_BASE_URL}/comment/user/${userId}`,
    COMMENT_CREATE: `${API_BASE_URL}/comment/create`,
    COMMENT_DELETE: (id) => `${API_BASE_URL}/comment/${id}`,
    COMMENT_LIKE: (id) => `${API_BASE_URL}/comment/like/${id}`
};
