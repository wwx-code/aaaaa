<template>
	<!--随机文章-->
	<div class="ui segments m-box">
		<div class="ui secondary segment"><i class="bookmark icon"></i>随机文章</div>
		<div class="ui yellow segment">
			<div class="ui divided items">
				<div class="item" v-for="blog in randomBlogList" :key="blog.id">
					<div class="content">
						<a href="javascript:;" @click.prevent="toBlog(blog)" class="header m-text-500">{{ blog.title }}</a>
						<div class="meta">
							<router-link :to="`/category/${blog.category.categoryName}`">
								<i class="folder open icon"></i>{{ blog.category.categoryName }}
							</router-link>
						</div>
						<div class="extra">
							<router-link :to="`/tag/${tag.tagName}`" class="ui label m-text-500" :style="{background:tag.color,color:'#fff'}" v-for="(tag,index) in blog.tags" :key="index">{{ tag.tagName }}</router-link>
							<a href="javascript:;" @click.prevent="toBlog(blog)" class="ui right floated">
								阅读全文<i class="angle double right icon"></i>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "RandomBlog",
		props: {
			randomBlogList: {
				type: Array,
				required: true
			},
		},
		methods: {
			toBlog(blog) {
				this.$store.dispatch('goBlogPage', blog)
			}
		}
	}
</script>

<style scoped>
	.secondary.segment {
		padding: 10px;
	}

	.header {
		font-size: 16px !important;
	}

	.meta {
		margin: 10px 0 !important;
	}

	.meta a {
		color: #000 !important;
	}
</style>