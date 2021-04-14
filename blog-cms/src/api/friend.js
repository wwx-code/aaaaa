import axios from "axios";

export function getFriendsByQuery(queryInfo) {
	return axios({
		url: 'friends',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function updatePublished(id, isPublished) {
	return axios({
		url: 'friend/published',
		method: 'GET',
		params: {
			id,
			isPublished
		}
	})
}

export function saveFriend(form) {
	return axios({
		url: 'friend/edit',
		method: 'POST',
		data: {
			...form
		}
	})
}

export function updateFriend(form) {
	return axios({
		url: 'friend/edit',
		method: 'POST',
		data: {
			...form
		}
	})
}

export function deleteFriendById(id) {
	return axios({
		url: 'friend/delete',
		method: 'GET',
		params: {
			id
		}
	})
}

export function getFriendInfo() {
	return axios({
		url: 'friendInfo',
		method: 'GET'
	})
}

export function updateContent(content) {
	return axios({
		url: 'updateFriendInfo',
		method: 'POST',
		data: {
			content
		}
	})
}