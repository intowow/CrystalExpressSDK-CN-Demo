﻿<h3 id='before' style='color:red'>檢查要點</h3>
<table border="1">
	<thead>
		<tr>
			<td style='color:green'>檢查大項</td><td align="center" style='color:green'>內容</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td align="center">
				Activity前景與背景
			</td>
			<td>
				<a target="_blank" href="../activity_setting">確認每隻Activity繼承BaseActivity</a>
			</td>
		</tr>
		<tr>
			<td align="center">
				開機與插頁蓋屏
			</td>
			<td>
				<a target="_blank" href="../opensplash/#OpenSplash-onConfigurationChanged">可正常撥放橫式蓋屏廣告</a>
			</td>
		</tr>
		<tr>
			<td align="center">
				信息流廣告
			</td>
			<td>
				<ul>
					<li><a target="_blank" href="../stream/#stream_adload">可正常出現廣告</a></li>
					<li><a target="_blank" href="../stream/#stream_itemclick">點擊廣告的上下則文章可正常執行</a></li>
					<li><a target="_blank" href="../stream/#stream_scroll_status">上下滑動列表後，可於SCROLL_STATE_IDLE的狀態下撥放視頻</a></li>
					<li><a target="_blank" href="../stream/#stream_active">左右切換列表後，可於SCROLL_STATE_IDLE的狀態下撥放視頻</a></li>
					<li><a target="_blank" href="../stream/#stream_notify">當列表刷新資料時，廣告可正確安插至原位置，且不會覆蓋新資料</a></li>
					<li><a target="_blank" href="../stream/#stream_activity">開啟視頻音樂後，按HOME退出應用程式時，可正常暫停視頻</a></li>
				</ul>
			</td>
		</tr>
		<tr>
			<td align="center">
				內文廣告
			</td>
			<td>
				<a target="_blank" href="../content/#content_life">按HOME退出應用程式後，可正常暫停視頻 ;再進入應用程式後，銀幕上的視頻廣告可再啟動</a>
			</td>
		</tr>
		<tr>
			<td align="center">預覽模式</td>
			<td>
				<a target="_blank" href="../preview">掃描QRCode可正常使用預覽模式</a>
			</td>
		</tr>
	</tbody>
</table>

<br/>
<br/>
