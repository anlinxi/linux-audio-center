(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0af799ff"],{"0440":function(r,e,t){var o=t("8659"),n=t("992d"),a=t("dad8"),c=Error.captureStackTrace;r.exports=function(r,e,t,s){a&&(c?c(r,e):o(r,"stack",n(t,s)))}},"20ec":function(r,e,t){var o=t("d02c"),n=t("ca86"),a=t("98d4"),c=t("bc07"),s=c("toStringTag"),i=Object,u="Arguments"==a(function(){return arguments}()),l=function(r,e){try{return r[e]}catch(t){}};r.exports=o?a:function(r){var e,t,o;return void 0===r?"Undefined":null===r?"Null":"string"==typeof(t=l(e=i(r),s))?t:u?a(e):"Object"==(o=a(e))&&n(e.callee)?"Arguments":o}},"2b09":function(r,e,t){var o=t("3e77"),n=t("76db"),a=t("5fae");r.exports=Object.setPrototypeOf||("__proto__"in{}?function(){var r,e=!1,t={};try{r=o(Object.prototype,"__proto__","set"),r(t,[]),e=t instanceof Array}catch(c){}return function(t,o){return n(t),a(o),e?r(t,o):t.__proto__=o,t}}():void 0)},"2e9b":function(r,e,t){var o=t("d771").f;r.exports=function(r,e,t){t in r||o(r,t,{configurable:!0,get:function(){return e[t]},set:function(r){e[t]=r}})}},"3e77":function(r,e,t){var o=t("90ea"),n=t("e343");r.exports=function(r,e,t){try{return o(n(Object.getOwnPropertyDescriptor(r,e)[t]))}catch(a){}}},"4df0":function(r,e,t){var o=t("ca86"),n=t("fbae"),a=t("2b09");r.exports=function(r,e,t){var c,s;return a&&o(c=e.constructor)&&c!==t&&n(s=c.prototype)&&s!==t.prototype&&a(r,s),r}},"5c42":function(r,e,t){"use strict";t("92f7")},"5fae":function(r,e,t){var o=t("ca86"),n=String,a=TypeError;r.exports=function(r){if("object"==typeof r||o(r))return r;throw a("Can't set "+n(r)+" as a prototype")}},7462:function(r,e,t){var o=t("20ec"),n=String;r.exports=function(r){if("Symbol"===o(r))throw TypeError("Cannot convert a Symbol value to a string");return n(r)}},7612:function(r,e,t){"use strict";var o=t("11ea"),n=t("23fe"),a=t("8659"),c=t("f1b1"),s=t("2b09"),i=t("fa50"),u=t("2e9b"),l=t("4df0"),f=t("a790"),p=t("9716"),m=t("0440"),d=t("1966"),g=t("01dc");r.exports=function(r,e,t,b){var v="stackTraceLimit",h=b?2:1,y=r.split("."),F=y[y.length-1],k=o.apply(null,y);if(k){var x=k.prototype;if(!g&&n(x,"cause")&&delete x.cause,!t)return k;var w=o("Error"),E=e((function(r,e){var t=f(b?e:r,void 0),o=b?new k(r):new k;return void 0!==t&&a(o,"message",t),m(o,E,o.stack,2),this&&c(x,this)&&l(o,this,E),arguments.length>h&&p(o,arguments[h]),o}));if(E.prototype=x,"Error"!==F?s?s(E,w):i(E,w,{name:!0}):d&&v in k&&(u(E,k,v),u(E,k,"prepareStackTrace")),i(E,k),!g)try{x.name!==F&&a(x,"name",F),x.constructor=E}catch(_){}return E}}},"8c9b":function(r,e,t){var o=t("31c1"),n=t("9978"),a=t("c8c7"),c=t("7612"),s="WebAssembly",i=n[s],u=7!==Error("e",{cause:7}).cause,l=function(r,e){var t={};t[r]=c(r,e,u),o({global:!0,constructor:!0,arity:1,forced:u},t)},f=function(r,e){if(i&&i[r]){var t={};t[r]=c(s+"."+r,e,u),o({target:s,stat:!0,constructor:!0,arity:1,forced:u},t)}};l("Error",(function(r){return function(e){return a(r,this,arguments)}})),l("EvalError",(function(r){return function(e){return a(r,this,arguments)}})),l("RangeError",(function(r){return function(e){return a(r,this,arguments)}})),l("ReferenceError",(function(r){return function(e){return a(r,this,arguments)}})),l("SyntaxError",(function(r){return function(e){return a(r,this,arguments)}})),l("TypeError",(function(r){return function(e){return a(r,this,arguments)}})),l("URIError",(function(r){return function(e){return a(r,this,arguments)}})),f("CompileError",(function(r){return function(e){return a(r,this,arguments)}})),f("LinkError",(function(r){return function(e){return a(r,this,arguments)}})),f("RuntimeError",(function(r){return function(e){return a(r,this,arguments)}}))},"92f7":function(r,e,t){},9716:function(r,e,t){var o=t("fbae"),n=t("8659");r.exports=function(r,e){o(e)&&"cause"in e&&n(r,"cause",e.cause)}},"992d":function(r,e,t){var o=t("90ea"),n=Error,a=o("".replace),c=function(r){return String(n(r).stack)}("zxcasd"),s=/\n\s*at [^:]*:[^\n]*/,i=s.test(c);r.exports=function(r,e){if(i&&"string"==typeof r&&!n.prepareStackTrace)while(e--)r=a(r,s,"");return r}},a790:function(r,e,t){var o=t("7462");r.exports=function(r,e){return void 0===r?arguments.length<2?"":e:o(r)}},c8c7:function(r,e,t){var o=t("32c6"),n=Function.prototype,a=n.apply,c=n.call;r.exports="object"==typeof Reflect&&Reflect.apply||(o?c.bind(a):function(){return c.apply(a,arguments)})},d02c:function(r,e,t){var o=t("bc07"),n=o("toStringTag"),a={};a[n]="z",r.exports="[object z]"===String(a)},d5c2:function(r,e,t){"use strict";t.r(e);var o=function(){var r=this,e=r._self._c;return e("div",{staticClass:"register"},[e("el-form",{ref:"ruleForm",staticClass:"demo-ruleForm",attrs:{model:r.ruleForm,rules:r.rules}},[e("el-form-item",{attrs:{label:"",prop:"loginCode"}},[e("el-input",{attrs:{type:"text",autocomplete:"off","prefix-icon":"el-icon-user-solid",placeholder:"请输入用户名"},model:{value:r.ruleForm.loginCode,callback:function(e){r.$set(r.ruleForm,"loginCode",e)},expression:"ruleForm.loginCode"}})],1),e("el-form-item",{attrs:{label:"",prop:"userName"}},[e("el-input",{attrs:{type:"text",autocomplete:"off","prefix-icon":"el-icon-user-solid",placeholder:"请输入用户昵称"},model:{value:r.ruleForm.userName,callback:function(e){r.$set(r.ruleForm,"userName",e)},expression:"ruleForm.userName"}})],1),e("el-form-item",{attrs:{label:"",prop:"password"}},[e("el-input",{attrs:{type:"password",autocomplete:"off","prefix-icon":"el-icon-lock",placeholder:"请输入密码"},model:{value:r.ruleForm.password,callback:function(e){r.$set(r.ruleForm,"password",e)},expression:"ruleForm.password"}})],1),e("el-form-item",{attrs:{label:"",prop:"checkPass"}},[e("el-input",{attrs:{type:"password",autocomplete:"off","prefix-icon":"el-icon-lock",placeholder:"确认密码"},model:{value:r.ruleForm.checkPass,callback:function(e){r.$set(r.ruleForm,"checkPass",e)},expression:"ruleForm.checkPass"}})],1),e("el-form-item",{attrs:{label:"",prop:"mgrType"}},[e("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:"请选择账户类型"},model:{value:r.ruleForm.mgrType,callback:function(e){r.$set(r.ruleForm,"mgrType",e)},expression:"ruleForm.mgrType"}},[e("el-option",{attrs:{label:"普通用户",value:"0"}}),e("el-option",{attrs:{label:"系统管理员",value:"1"}}),e("el-option",{attrs:{label:"二级管理员",value:"2"}})],1)],1),e("el-form-item",{staticClass:"btns"},[e("el-button",{attrs:{type:"primary"},on:{click:function(e){return r.submitForm("ruleForm")}}},[r._v("注册")]),e("el-button",{on:{click:function(e){return r.resetForm("ruleForm")}}},[r._v("重置")])],1)],1)],1)},n=[],a=(t("8c9b"),{data(){var r=(r,e,t)=>{""===e?t(new Error("请输入密码")):(""!==this.ruleForm.checkPass&&this.$refs.ruleForm.validateField("checkPass"),t())},e=(r,e,t)=>{""===e?t(new Error("请再次输入密码")):e!==this.ruleForm.password?t(new Error("两次输入密码不一致!")):t()};return{activeName:"second",ruleForm:{loginCode:"",password:"",checkPass:""},rules:{loginCode:[{required:!0,message:"请输入您的名称",trigger:"blur"},{min:2,max:10,message:"长度在 2 到 10 个字符",trigger:"blur"}],userName:[{required:!0,message:"请输入您的昵称",trigger:"blur"}],password:[{required:!0,validator:r,trigger:"blur"}],checkPass:[{required:!0,validator:e,trigger:"blur"}],mgrType:[{required:!0,message:"请选择账户类型",trigger:"blur"}]}}},methods:{submitForm(r){const e=this;this.$refs[r].validate(r=>{if(!r)return console.log("error submit!!"),!1;e.$http.common("/app/register",this.ruleForm).then(r=>{window.console.log("[信息]提交注册信息",r),200===r.status&&"true"===r.data.result?(this.$message({type:"success",message:r.data.message}),e.activeName="first"):this.$message({type:"error",message:"注册异常:"+r.data.message})})})},resetForm(r){this.$refs[r].resetFields()}}}),c=a,s=(t("5c42"),t("e607")),i=Object(s["a"])(c,o,n,!1,null,"6e71a468",null);e["default"]=i.exports},dad8:function(r,e,t){var o=t("e71d"),n=t("1dd0");r.exports=!o((function(){var r=Error("a");return!("stack"in r)||(Object.defineProperty(r,"stack",n(1,7)),7!==r.stack)}))}}]);
//# sourceMappingURL=chunk-0af799ff.4167ad81.js.map