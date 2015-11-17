## 金山词霸字典解析、生成器及sk引擎的java简单实现 ##

最终目的是将朗文当代英语做成金山的格式，欢迎各位爱好者的加入

converter 使用示例：


导出一个dic文件：
```
converter -d "D:\1#520.DIC" "D:\desFloder"
when job done desFolder should contain the following files:
 1#520.DIC.zip(data file)
 1#520.DIC.txt(index file with word and fileName key-value pair)
 1#520.DIC.inf(dict info)
```
生成一个dic文件：
```
converter -g "D:\srcFolder" "D:\1#520.DIC"
the srcFolder should contain the following files:
 1#520.DIC.zip
 1#520.DIC.txt
 1#520.DIC.inf
when job done, D:\1#520.DIC should be generated
```