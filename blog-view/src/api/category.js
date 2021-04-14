import axios from "axios";

export function getBlogListByCategoryName(pageNum, categoryName) {
	return axios({
		url: 'category',
		method: 'GET',
		params: {
			pageNum,
			categoryName
		}
	})
}