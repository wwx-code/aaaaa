import axios from "axios";

export function getMomentListByPageNum(pageNum) {
	return axios({
		url: 'moments',
		method: 'GET',
		// headers: {
		// 	Authorization: token,
		// },
		params: {
			pageNum
		}
	})
}

export function likeMoment(id) {
	return axios({
		url: 'moment/like',
		method: 'POST',
		params: {
			id
		}
	})
}