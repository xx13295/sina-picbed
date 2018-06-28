# 新浪图床

	新浪图床是个啥玩意应该不用解释了吧。
	
	如果你觉得代码好用 点一个 小星星 呀

	
----------------------------
	食用方法     
----------------------------
	
	本demo 基于springboot 如果对springboot 不是很了解的 童鞋 可以先 去学习一下 
	不过问题不大 按照下面的步骤 搞就完事了。
	
	 修改 application.properties 配置文件
	 
	 填写自己的 新浪 账户 名和密码  
	 
	运行 SinaPicbedApplication.java 
	
	打开
	http://127.0.0.1:1024/
	
	即可上传图片到新浪图床
	
	
	图片大小分为 "large", "mw1024", "mw690", "bmiddle", "small", "thumb180", "thumbnail", "square" 
	
	根据 自己需求 选择 即可，就算 一开始选择的 large 我突然想要square方形的图片 直接将链接里的large 换成square 效果也是一样的。
	
	点击完提交按钮后  按F12 就可以在 console控制台中获取 到图片的链接。
	
	
	
	