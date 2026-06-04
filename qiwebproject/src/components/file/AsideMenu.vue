<template>
	<div class="side-menu-wrapper">
		<!-- 768px 下，以抽屉形式展示 -->
		<el-drawer
			:visible.sync="isDrawer"
			:with-header="false"
			:size="210"
			direction="ltr"
			v-if="screenWidth <= 768"
		>
			<!-- collapse 属性：控制菜单收缩展开 -->
			<el-menu
				class="side-menu"
				:default-active="activeIndex"
				:router="true"
				:collapse="isCollapse"
			>
				<el-submenu index="myFile" class="my-file">
					<template slot="title">
						<!-- 图标均来自 Element UI 官方图标库 https://element.eleme.cn/#/zh-CN/component/icon -->
						<i class="el-icon-files"></i>
						<span slot="title">我的文件</span>
					</template>
					<el-menu-item
						index="0"
						:route="{ name: 'File', query: { fileType: 0, filePath: '/' } }"
					>
						<i class="el-icon-menu"></i>
						<span slot="title">全部</span>
					</el-menu-item>
					<el-menu-item
						index="1"
						:route="{ name: 'File', query: { fileType: 1 } }"
					>
						<i class="el-icon-picture-outline"></i>
						<span slot="title">图片</span>
					</el-menu-item>
					<el-menu-item
						index="2"
						:route="{ name: 'File', query: { fileType: 2 } }"
					>
						<i class="el-icon-document"></i>
						<span slot="title">文档</span>
					</el-menu-item>
					<el-menu-item
						index="3"
						:route="{ name: 'File', query: { fileType: 3 } }"
					>
						<i class="el-icon-video-camera"></i>
						<span slot="title">视频</span>
					</el-menu-item>
					<el-menu-item
						index="4"
						:route="{ name: 'File', query: { fileType: 4 } }"
					>
						<i class="el-icon-headset"></i>
						<span slot="title">音乐</span>
					</el-menu-item>
					<el-menu-item
						index="5"
						:route="{ name: 'File', query: { fileType: 5 } }"
					>
						<i class="el-icon-takeaway-box"></i>
						<span slot="title">其他</span>
					</el-menu-item>
				</el-submenu>
				<el-menu-item
					index="9"
					:route="{ name: 'File', query: { fileType: 9, filePath: '/' } }"
					class="favorites"
				>
					<i class="el-icon-star-on"></i>
					<span slot="title">收藏夹</span>
				</el-menu-item>
				<el-menu-item
					index="6"
					:route="{ name: 'File', query: { fileType: 6 } }"
					class="recovery"
				>
					<i class="el-icon-delete"></i>
					<span slot="title">回收站</span>
				</el-menu-item>
				<el-menu-item
					index="8"
					:route="{ name: 'File', query: { fileType: 8, filePath: '/' } }"
					class="my-share"
				>
					<i class="el-icon-share"></i>
					<span slot="title">我的分享</span>
				</el-menu-item>
				<el-submenu index="tags" class="tags-group" v-if="tags.length > 0">
					<template slot="title">
						<i class="el-icon-price-tag"></i>
						<span slot="title">标签</span>
					</template>
					<el-menu-item
						v-for="tag in tags"
						:key="tag.tagId"
						:index="'tag_' + tag.tagId"
						@click="handleTagClick(tag)"
					>
						<span
							class="tag-color-dot"
							:style="{ background: tag.tagColor }"
						></span>
						<span slot="title">{{ tag.tagName }}</span>
					</el-menu-item>
						<el-menu-item index="tag_manage" class="tag-manage-entry" @click="handleTagManageClick">
							<i class="el-icon-setting"></i>
							<span slot="title">管理标签</span>
						</el-menu-item>

				</el-submenu>
			</el-menu>
			<!-- 存储信息显示 -->
			<div class="storage-wrapper" :class="{ fold: isCollapse }">
				<el-progress
					:percentage="storagePercentage"
					:color="storageColor"
					:show-text="false"
					:type="isCollapse ? 'circle' : 'line'"
					:width="32"
					:stroke-width="isCollapse ? 4 : 6"
					stroke-linecap="square"
				></el-progress>
				<div class="text" v-show="!isCollapse">
					<span class="label">存储</span>
					<span>
						{{ $file.calculateFileSize(storageValue) }} /
						{{ $file.calculateFileSize(totalStorageValue) }}
					</span>
				</div>
				<div class="text" v-show="isCollapse">
					<span>{{ $file.calculateFileSize(storageValue) }}</span>
				</div>
			</div>
		</el-drawer>
		<!-- 768px 以上，平铺展示 -->
		<template v-else>
			<!-- collapse 属性：控制菜单收缩展开 -->
			<el-menu
				class="side-menu"
				:default-active="activeIndex"
				:router="true"
				:collapse="isCollapse"
			>
				<el-submenu index="myFile" class="my-file">
					<template slot="title">
						<!-- 图标均来自 Element UI 官方图标库 https://element.eleme.cn/#/zh-CN/component/icon -->
						<i class="el-icon-files"></i>
						<span slot="title">我的文件</span>
					</template>
					<el-menu-item
						index="0"
						:route="{ name: 'File', query: { fileType: 0, filePath: '/' } }"
					>
						<i class="el-icon-menu"></i>
						<span slot="title">全部</span>
					</el-menu-item>
					<el-menu-item
						index="1"
						:route="{ name: 'File', query: { fileType: 1 } }"
					>
						<i class="el-icon-picture-outline"></i>
						<span slot="title">图片</span>
					</el-menu-item>
					<el-menu-item
						index="2"
						:route="{ name: 'File', query: { fileType: 2 } }"
					>
						<i class="el-icon-document"></i>
						<span slot="title">文档</span>
					</el-menu-item>
					<el-menu-item
						index="3"
						:route="{ name: 'File', query: { fileType: 3 } }"
					>
						<i class="el-icon-video-camera"></i>
						<span slot="title">视频</span>
					</el-menu-item>
					<el-menu-item
						index="4"
						:route="{ name: 'File', query: { fileType: 4 } }"
					>
						<i class="el-icon-headset"></i>
						<span slot="title">音乐</span>
					</el-menu-item>
					<el-menu-item
						index="5"
						:route="{ name: 'File', query: { fileType: 5 } }"
					>
						<i class="el-icon-takeaway-box"></i>
						<span slot="title">其他</span>
					</el-menu-item>
				</el-submenu>
				<el-menu-item
					index="9"
					:route="{ name: 'File', query: { fileType: 9, filePath: '/' } }"
					class="favorites"
				>
					<i class="el-icon-star-on"></i>
					<span slot="title">收藏夹</span>
				</el-menu-item>
				<el-menu-item
					index="6"
					:route="{ name: 'File', query: { fileType: 6 } }"
					class="recovery"
				>
					<i class="el-icon-delete"></i>
					<span slot="title">回收站</span>
				</el-menu-item>
				<el-menu-item
					index="8"
					:route="{ name: 'File', query: { fileType: 8, filePath: '/' } }"
					class="my-share"
				>
					<i class="el-icon-share"></i>
					<span slot="title">我的分享</span>
				</el-menu-item>
				<el-submenu index="tags" class="tags-group" v-if="tags.length > 0">
					<template slot="title">
						<i class="el-icon-price-tag"></i>
						<span slot="title">标签</span>
					</template>
					<el-menu-item
						v-for="tag in tags"
						:key="tag.tagId"
						:index="'tag_' + tag.tagId"
						@click="handleTagClick(tag)"
					>
						<span
							class="tag-color-dot"
							:style="{ background: tag.tagColor }"
						></span>
						<span slot="title">{{ tag.tagName }}</span>
					</el-menu-item>
						<el-menu-item index="tag_manage" class="tag-manage-entry" @click="handleTagManageClick">
							<i class="el-icon-setting"></i>
							<span slot="title">管理标签</span>
						</el-menu-item>

				</el-submenu>
			</el-menu>
			<!-- 存储信息显示 -->
			<div class="storage-wrapper" :class="{ fold: isCollapse }">
				<el-progress
					:percentage="storagePercentage"
					:color="storageColor"
					:show-text="false"
					:type="isCollapse ? 'circle' : 'line'"
					:width="32"
					:stroke-width="isCollapse ? 4 : 6"
					stroke-linecap="square"
				></el-progress>
				<div class="text" v-show="!isCollapse">
					<span class="label">存储</span>
					<span
						>{{ $file.calculateFileSize(storageValue) }} /
						{{ $file.calculateFileSize(totalStorageValue) }}</span
					>
				</div>
				<div class="text" v-show="isCollapse">
					<span>{{ $file.calculateFileSize(storageValue) }}</span>
				</div>
			</div>
		</template>
		<!-- 展开 & 收缩分类栏 -->
		<el-tooltip
			effect="dark"
			:content="isCollapse ? '展开' : '收起'"
			placement="right"
		>
			<div
				class="aside-title"
				@click="isCollapse ? (isCollapse = false) : (isCollapse = true)"
			>
				<i
					class="icon"
					:class="isCollapse ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'"
					:title="isCollapse ? '展开' : '收起'"
				></i>
			</div>
		</el-tooltip>
	</div>
</template>

<script>
import { getTagList } from '_r/tag.js'

export default {
	name: 'SideMenu',
	data() {
		return {
			isDrawer: false, //  控制移动端菜单抽屉是否显示
			isCollapse: false, //  控制菜单收缩展开
			tags: [], //  用户标签列表
			// 菜单 index 和名称 Map
			myFileMenuMap: {
				0: '全部',
				1: '图片',
				2: '文档',
				3: '视频',
				4: '音乐',
				5: '其他',
				6: '回收站',
				8: '我的分享',
				9: '收藏夹'
			},
			//  自定义进度条颜色，不同占比，进度条颜色不同
			storageColor: [
				{ color: '#67C23A', percentage: 50 },
				{ color: '#E6A23C', percentage: 80 },
				{ color: '#F56C6C', percentage: 100 }
			]
		}
	},
	computed: {
		// 当前激活菜单的 index
		activeIndex() {
			// 如果当前按标签筛选，返回标签对应的 index
			if (this.$route.query.tagId) {
				return 'tag_' + this.$route.query.tagId
			}
			return String(this.$route.query.fileType) //  获取当前路由参数中包含的文件类型
		},
		// 存储容量
		storageValue() {
			return this.$store.state.sideMenu.storageValue
		},
		totalStorageValue() {
			return this.$store.state.sideMenu.totalStorageValue
		},
		// 存储百分比
		storagePercentage() {
			return this.totalStorageValue
				? (this.storageValue / this.totalStorageValue) * 100
				: 0
		},
		// 屏幕宽度
		screenWidth() {
			return this.$store.state.common.screenWidth
		}
	},
	watch: {
		// 监听左侧菜单切换，修改浏览器标签标题
		activeIndex(newValue) {
			const numValue = Number(newValue)
			if (this.myFileMenuMap[numValue]) {
				document.title = `${this.myFileMenuMap[numValue]} - ${this.$config.siteName}`
			}
			this.isDrawer = false
		},
		// 监听收缩状态变化，存储在 localStorage 中，保证页面刷新时仍然保存设置的状态
		isCollapse(newValue) {
			localStorage.setItem('qiwen_is_collapse', newValue)
			if (this.screenWidth <= 768 && newValue) {
				this.isDrawer = true
				this.isCollapse = false
			}
		}
	},
	created() {
		this.isCollapse = localStorage.getItem('qiwen_is_collapse') === 'true' //  读取保存的状态
		this.loadTags()
	},
	mounted() {
		const numValue = Number(this.activeIndex)
		if (this.myFileMenuMap[numValue]) {
			document.title = `${this.myFileMenuMap[numValue]} - ${this.$config.siteName}`
		}
	},
	methods: {
		/**
		 * 加载用户标签列表
		 */
		loadTags() {
			getTagList().then((res) => {
				if (res.success) {
					this.tags = res.dataList || []
				}
			})
		},
		/**
		 * 点击标签，跳转到按标签筛选的文件列表
		 * @param {object} tag 标签对象
		 */
		handleTagClick(tag) {
			this.$router.push({
				name: 'File',
				query: { tagId: tag.tagId, filePath: '/' }
			})
		},

			/**
			 * 跳转到标签管理页面
			 */
			handleTagManageClick() {
				this.$router.push({ name: 'TagManage' })
			},

	}
}
</script>

<style lang="stylus" scoped>
@import '~_a/styles/varibles.styl';
@import '~_a/styles/mixins.styl';

.side-menu-wrapper {
  position: relative;
  height: calc(100vh - 61px);
  padding-right: 11px;
  .side-menu {
    // 高度设置为屏幕高度减去顶部导航栏的高度
    height: calc(100vh - 127px);
    overflow: auto;
    // 调整滚动条样式
    setScrollbar(6px, transparent, #C0C4CC);
    .el-menu-item.is-active {
      background: #ecf5ff;
    }
    .my-file, .recovery, .favorites {
      box-shadow: 0 4px 12px 0 $BorderExtralight;
    }
    .tag-color-dot {
      display: inline-block;
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-right: 8px;
      vertical-align: middle;
    }
	    .tag-manage-entry {
	      border-top: 1px solid #EBEEF5;
	      margin-top: 4px;
	      padding-top: 4px;
	      font-size: 12px;
	      color: #909399;
	    }

  }
  >>> .el-menu {
    background: transparent;
  }
  // 对展开状态下的菜单设置宽度
  .side-menu:not(.el-menu--collapse) {
    width: 210px;
  }
  // 存储空间展示区
  .storage-wrapper {
    position: absolute;
    bottom: 0;
    left: 0;
    box-shadow: 0 -2px 12px 0 $BorderExtralight;
    border-right: solid 1px #e6e6e6;
    box-sizing: border-box;
    width: calc(100% - 11px);
    height: 66px;
    padding: 16px;
    z-index: 2;
    color: $PrimaryText;
    .text {
      margin-top: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 12px;
      flex-wrap: wrap;
    }
  }
  .storage-wrapper.fold {
    padding: 0;
    >>> .el-progress--circle {
      margin: 0 auto;
      width: 32px;
      display: block;
    }
    .text {
      font-size: 12px;
      justify-content: center;
    }
  }
  // 折叠图标调整样式
  .aside-title {
    position: absolute;
    top: calc(50% - 50px);
    right: 0;
    z-index: 2;
    background: $BorderBase;
    color: #fff;
    width: 12px;
    height: 100px;
    line-height: 100px;
    cursor: pointer;
    border-radius: 0 16px 16px 0;
    &:hover {
      opacity: 0.7;
    }
    .icon {
      font-size: 12px;
    }
  }
}
</style>
