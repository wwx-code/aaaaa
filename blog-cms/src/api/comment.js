import axios from "axios";

export function getCommentListByQuery(queryInfo) {
	return axios({
		url: 'comments',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function getBlogList() {
	return axios({
		url: 'getBlogIdAndTitle',
		method: 'GET'
	})
}

export function updatePublished(id, isPublished) {
	return axios({
		url: 'comment/published',
		method: 'GET',
		params: {
			id,
			isPublished
		}
	})
}

export function updateNotice(id, isNotice) {
	return axios({
		url: 'comment/notice',
		method: 'GET',
		params: {
			id,
			isNotice
		}
	})
}

export function deleteCommentById(id) {
	return axios({
		url: 'comment/delete',
		method: 'GET',
		params: {
			id
		}
	})
}

export function editComment(form) {
	return axios({
		url: 'comment/update',
		method: 'POST',
		data: {
			...form
		}
	})
}