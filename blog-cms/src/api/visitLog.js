import axios from "axios";

export function getVisitLogList(queryInfo) {
	return axios({
		url: 'visitorLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteVisitLogById(id) {
	return axios({
		url: 'visitorLog/delete',
		method: 'GET',
		params: {
			id
		}
	})
}