import axios from "axios";

export function getMomentListByQuery(queryInfo) {
	return axios({
		url: 'moments',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function updatePublished(id, isPublished) {
	return axios({
		url: 'moment/publish',
		method: 'GET',
		params: {
			id,
			isPublished
		}
	})
}

export function getMomentById(id) {
	return axios({
		url: 'moment',
		method: 'GET',
		params: {
			id
		}
	})
}

export function deleteMomentById(id) {
	return axios({
		url: 'moment/delete',
		method: 'GET',
		params: {
			id
		}
	})
}

export function saveMoment(moment) {
	return axios({
		url: 'moment/edit',
		method: 'POST',
		data: {
			...moment
		}
	})
}

export function updateMoment(moment) {
	return axios({
		url: 'moment/edit',
		method: 'POST',
		data: {
			...moment
		}
	})
}