function selectAll() {
			var chkAll = document.getElementById("chkAll");
			var chk = document.getElementsById("chk");
			if (chkAll.checked == true) {
				for (var i = 0; i < chk.length; i++) {
					chk[i].checked = true;
				}
			} else {
				for (var i = 0; i < chk.length; i++) {
					chk[i].checked = false;
				}
			}
			document.getElementsByClassName("chk")
		}