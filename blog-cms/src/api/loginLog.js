import axios from "axios";

export function getLoginLogList(queryInfo) {
	return axios({
		url: 'loginLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteLoginLogById(id) {
	return axios({
		url: 'loginLog/delete',
		method: 'GET',
		params: {
			id
		}
	})
}