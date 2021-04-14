import axios from "axios";

export function getAbout() {
	return axios({
		url: 'about',
		method: 'GET'
	})
}

export function updateAbout(form) {
	return axios({
		url: 'updateAbout',
		method: 'POST',
		data: {
			...form
		}
	})
}