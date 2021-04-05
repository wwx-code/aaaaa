import axios from "axios";

export function getData(queryInfo) {
	return axios({
		url: 'categories',
		method: 'GET',
		params: {
			...queryInfo
		}
	})
}

export function addCategory(form) {
	return axios({
		url: 'saveCategory',
		method: 'POST',
		data: {
			...form
		}
	})
}

export function editCategory(form) {
	return axios({
		url: 'updateCategory',
		method: 'POST',
		data: {
			...form
		}
	})
}

export function deleteCategoryById(id) {
	return axios({
		url: 'deleteCategory',
		method: 'GET',
		params: {
			id
		}
	})
}