import axios from "axios";

export function getExceptionLogList(queryInfo) {
	return axios({
		url: 'exceptionLogs',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function deleteExceptionLogById(id) {
	return axios({
		url: 'exceptionLog/delete',
		method: 'GET',
		params: {
			id
		}
	})
}