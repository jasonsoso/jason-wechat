<%@ page contentType="text/html;charset=utf-8" %>
<%@include file="/common/taglibs.jsp" %>

<ul>
<c:choose>
	<c:when test="${page.pageNo == 1 }">
		<li><a>&lt;&lt; 首页</a></li>
	</c:when>
	<c:otherwise>
		<li><a href="?pageNo=1${param.searchParam}">&lt;&lt; 首页</a></li>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${page.isHasPre}">
		<li><a href="?pageNo=${page.prePage}${param.searchParam}">&lt; 上一页</a></li>
	</c:when>
	<c:otherwise>
		<li><a>&lt; 上一页</a></li>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${page.pageNo < 10 }">
		<c:forEach var="number" varStatus="" begin="1" end="${page.pageNo}">
			<c:choose>
				<c:when test="${page.pageNo == number }">
					<li class="active"><a>${number }</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="?pageNo=${number }${param.searchParam}">${number }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:when>
	<c:otherwise>
		...&nbsp;
		<c:forEach var="number" begin="${page.pageNo-4}" end="${page.pageNo}">
			<c:choose>
				<c:when test="${page.pageNo == number }">
					<li class="active"><a>${number }</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="?pageNo=${number }${param.searchParam}">${number }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:otherwise>
</c:choose>



<c:choose>
	<c:when test="${(page.pageNo>=(page.totalPages-4)) || ((page.totalPages-4)<=0) }">
		<c:if test="${(page.pageNo+1) <= page.totalPages }">
		<c:forEach var="number" varStatus="" begin="${page.pageNo+1}" end="${page.totalPages}">
			<li><a href="?pageNo=${number }${param.searchParam}">${number }</a></li>
			<c:if test="${page.pageNo == number }">
				<li class="active"><a>${number }</a></li>
			</c:if>
		</c:forEach>
		</c:if>
		
	</c:when>
	<c:otherwise>
		
		<c:forEach var="number" begin="${page.nextPage}" end="${page.pageNo+4}">
			<c:choose>
				<c:when test="${page.pageNo == number }">
					<li class="active"><a>${number }</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="?pageNo=${number }${param.searchParam}">${number }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		...&nbsp;
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${page.isHasNext}">
		<li><a href="?pageNo=${page.nextPage}${param.searchParam}">下一页 &gt;</a></li>
	</c:when>
	<c:otherwise>
		<li><a>下一页 &gt;</a></li>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${page.pageNo == page.totalPages }">
		<li><a>末页 &gt;&gt;</a></li>
	</c:when>
	<c:otherwise>
		<li><a href="?pageNo=${page.totalPages }${param.searchParam}">末页 &gt;&gt;</a></li>
	</c:otherwise>
</c:choose>

</ul>
<!--xxx/list?pageNo=2&pageSize=10&orderBy=&order= -->
