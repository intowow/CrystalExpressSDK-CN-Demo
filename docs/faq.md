﻿<h3 id='before' style='color:red'>問題集</h3>
<table border="1">
	<thead>
		<tr>
			<td style='color:green'>問題</td><td align="center" style='color:green'>說明</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td align="center">程式一執行便閃退</td>
			<td>
				<ul>
					<li>請確認<a target="_blank" href="../androidmanifest">AndroidManifest.xml</a>設定正確</li>
					<li>請確認AndroidManifest.xml裡的<a target="_blank" href="../androidmanifest/#meta-data">CRYSTAL_ID</a>為32個字元，不含空白</li>
					<li>若AndroidManifest.xml設定錯誤，可於DDMS裡看到<span style="color:red">please adding these properties in the AndroidManifest.xml</span>內容</li>
				</ul>
			</td>
		</tr>
		<tr>
			<td align="center">
				廣告沒有出現
			</td>
			<td>
				<ul>
					<li>若要使用測試模式，請確認在<a target="_blank" href="../activity_setting/#testmode">BaseActivity.java</a>使用I2WAPI.init(context, true)</li>
					<li>請確認網路狀態，視頻廣告只在wifi環境下才會下載</li>
					<li>同一隻手機上，不可有相同CrystalId的應用程式並存</li>
					<li>若操作過程想替換CrystalId，請移除應用程式，設定好AndroidManifest.xml後，再安裝測試</li>
					<li>若網速較慢，有可能是廣告素材還未下載完成</li>
					<li>請確認<a target="_blank" href="../naming/#placement">廣告版位名稱</a></li>
					<li>請確認<a target="_blank" href="../activity_setting">Activity設定</a>，讓Activity繼承BaseActivity</li>
					<li>該版位的廣告可能處於<a target="_blank" href="https://github.com/roylo/CrystalExpressDocumentation-iOS-zh_CN/blob/master/terminology.md/#user-content-ad-serving-control-廣告投放控制">廣告投放控制狀態</a>，可於後台修改</li>
				</ul>
			</td>
		</tr>
		<tr>
			<td align="center">
				整合開機蓋屏的頁面啟動時發生錯誤
			</td>
			<td>
				<ul>
					<li>Activity有實作onConfigurationChanged()</li>
					<li>AdroidManifest.xml有設定android:configChanges="orientation|screenSize"</li>
				</ul>
			</td>
		</tr>
	</tbody>
</table>

<br/>
<br/>
