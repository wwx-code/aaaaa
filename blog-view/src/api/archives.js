import axios from "axios";

export function getArchives() {
	return axios({
		url: 'archives',
		method: 'GET'
	})
}