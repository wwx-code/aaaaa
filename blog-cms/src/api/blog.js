import axios from "axios";

export function getDataByQuery(queryInfo) {
    return axios({
        url: 'blogs',
        method: 'GET',
        params: {
            ...queryInfo
        }
    })
}

export function deleteBlogById(id) {
    return axios({
        url: 'blog/delete',
        method: 'GET',
        params: {
            id
        }
    })
}

export function getCategoryAndTag() {
    return axios({
        url: 'categoryAndTag',
        method: 'GET'
    })
}

export function saveBlog(blog) {
    return axios({
        url: 'blog/add',
        method: 'POST',
        data: {
            ...blog
        }
    })
}

export function updateTop(id, isTop) {
    return axios({
        url: 'blog/top',
        method: 'GET',
        params: {
            id,
            isTop
        }
    })
}

export function updateRecommend(id, isRecommend) {
    return axios({
        url: 'blog/recommend',
        method: 'GET',
        params: {
            id,
            isRecommend
        }
    })
}

export function updateVisibility(id, form) {
    return axios({
        url: `blog/${id}/visibility`,
        method: 'POST',
        data: {
            ...form
        }
    })
}

export function getBlogById(id) {
    return axios({
        url: 'blog',
        method: 'GET',
        params: {
            id
        }
    })
}

export function updateBlog(blog) {
    return axios({
        url: 'blog/update',
        method: 'POST',
        data: {
            ...blog
        }
    })
}