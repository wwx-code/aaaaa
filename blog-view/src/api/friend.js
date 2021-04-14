import axios from "axios";

export function getFriendList() {
	return axios({
		url: 'friends',
		method: 'GET'
	})
}

export function getFriendInfo() {
	return axios({
		url: 'friendInfo',
		method: 'GET'
	})
}

export function addViewsByNickname(nickname) {
	return axios({
		url: 'addViews',
		method: 'POST',
		params: {
			nickname
		}
	})
}
