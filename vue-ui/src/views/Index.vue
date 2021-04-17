<template>
  <div>
    <nav-bar :buttons="baseButton" />
    <div class="content">
      <div id="display">
        <div class="information">
          <div style="height: 20%;width: 100%"></div>
          <div class="text-wrapper">
            <div class="important">
              Gabriel Web阅读器
            </div>
            <div class="text">
              支持txt、ePub、pdf等多种电子书格式
            </div>
            <div class="text">
              我们将尽全力为您打造舒适的阅读环境
            </div>
          </div>
          <div class="btn-wrapper">
            <div class="btn" id="register_btn" @click.prevent="$router.push('/register')">
              <span class="ripple"></span>
              <p>免费注册</p>
            </div>
            <br />
            <div class="btn" id="login_btn" @click.prevent="$router.push('/login')">
              <span class="ripple"></span>
              <p>登录账号</p>
            </div>
          </div>
        </div>

        <div class="line-wrapper">
          <div class="trait">
            云端存储
          </div>
          <div class="trait">
            多端同步
          </div>
          <div class="trait">
            多种格式
          </div>
          <div class="trait">
            简洁美观
          </div>
        </div>

        <div class="property-wrapper">
          <div style="text-align: center">
            <h1>特色</h1>
            <double-line width="60px" />
            <div class="property">
              基于云端存储的浏览器应用网络电子书阅读器，能够让您在任何地方使用任何拥有浏览器功能的设备阅读电子书
            </div>
          </div>
          <div class="box-wrapper">
            <div class="box">
              <div class="ico">
                <i class="el-icon-upload" />
              </div>
              <p class="title">
                云端存储与备份，数据自动同步
              </p>
              <span class="text">
                您需要登录账号后将书籍上传到云端，这将确保您以后能无障碍切换设备阅读，您的阅读进度也将一并保存，不论使用什么设备，您都可以从您上次的阅读进度开始继续阅读
              </span>
            </div>
            <div class="box">
              <div class="ico">
                <i class="el-icon-s-management" />
              </div>
              <p class="title">
                多种电子书格式支持，无需转换格式
              </p>
              <span class="text">
                目前Gabriel支持ePub、TXT、PDF三种格式的文件，后续根据需要可能会允许解析更多的电子书格式，以确保您无需手动转换格式便能阅读您的电子书
              </span>
            </div>
            <div class="box">
              <div class="ico">
                <i class="el-icon-document" />
              </div>
              <p class="title">
                书籍管理方便，流畅添加、删除书籍
              </p>
              <span class="text">
                您的书籍将统一在云端管理，所有数据与账户绑定，您可以在任何设备添加、删除您的电子书
              </span>
            </div>
          </div>
        </div>

        <div class="more">
          <el-collapse>
            <el-collapse-item title="为什么要选择Gabriel？">
              <p>Gabriel是目前少有的支持多种格式的Web阅读器，我们考察了很多阅读器，基本上只能支持epub或者txt格式，并且桌面端的程序对txt格式的电子书文件支持并不友好，在排版上也不尽人意，因此Gabriel在这方面做出了很多优化，并且会不断更新细节</p>
            </el-collapse-item>
            <el-collapse-item title="用户能上传多少书籍？">
              <p>目前Gabriel设定的每个用户上传书籍数量是10本，日后会根据需要增加普通用户的上传上限，甚至添加特权用户以确保您能够拥有更多书架位</p>
            </el-collapse-item>
            <el-collapse-item title="更多关于Gabriel">
              <p>
                Gabriel是基于本人在使用Neat-reader阅读电子书时遇到的种种问题而开发的Web应用<br />
                我曾尝试了各种桌面端电子书阅读程序，它们或多或少都有些不尽人意的地方，例如只能支持epub格式，或者对于特定格式排版出错<br />
                如果只使用移动端app阅读的话又难免会遇到进度无法同步的问题，因此我尝试开发了Gabriel，并且尝试将解决方法体现在它上面
              </p>
            </el-collapse-item>
          </el-collapse>
        </div>

        <div class="other-wrapper">
          <div class="element">
            <div class="text">
              Gabriel 阅读器<br />
              给你最好的阅读体验
            </div>
            <div class="btn" id="reg_btn" @click.prevent="$router.push('/register')">
              <span class="ripple"></span>
              <p>马上注册，免费体验</p>
            </div>
          </div>
        </div>

      </div>

      <div id="about">
        <div class="left">
          <div class="info">
            <span>Gabriel Reader</span>
            <br />
            <span>Gabriel是目前少有的支持多种格式多端同步的Web阅读器,我们将不断追求为用户提供更好的阅读体验</span>
            <div style="text-align: left">备案信息 浙ICP备20009938号-1</div>
          </div>
          <div class="divide"></div>
        </div>
        <div class="right">
          <div class="list">
            <div class="list-title">产品</div>
            <div class="list-body">
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
            </div>
          </div>
          <div class="list">
            <div class="list-title">公司</div>
            <div class="list-body">
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
              <a class="list-item">XX信息</a>
            </div>
          </div>
          <div class="support">
            <span>联系方式</span>
            <br />
            <span>用户服务（产品咨询/需求反馈/意见与建议）</span>
            <br />
            <span style="font-weight: normal">邮箱: postmaster@kyu92.top</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Index",
    components: {
      "nav-bar": () => import("@/components/NavBar"),
      "double-line": () => import("@/components/DoubleLine")
    },
    computed: {
      baseButton(){
        return this.$store.state.navBarButton;
      }
    },
    mounted() {
      let box = document.querySelectorAll(".btn");
      for(let i in box){
        if (!box.hasOwnProperty(i)){
          continue;
        }
        box[i].onmouseenter = function (event) {
          let ripple = box[i].querySelector(".ripple");
          ripple.classList.add("animation");
          ripple.style.width = this.offsetWidth + "px";
          ripple.style.height = this.offsetHeight + "px";
          ripple.style.top = -(this.offsetHeight - event.offsetY) + "px";
          ripple.style.left = -(this.offsetWidth - event.offsetX) + "px";
          setTimeout(function () {
            ripple.classList.remove("animation");
          }, 500);
        }
      }
    }
  }
</script>

<style scoped lang="less">
 .content{
   @import "../style/xs/index";
   @import "../style/sm/index";
   margin-top: 8vh;
   #display{
     .information{
       background-image: url("../assets/index-display.jpg");
       width: 100%;
       height: 85vh;
       background-size: cover;
       background-repeat: no-repeat;
       .text-wrapper{
         display: inline-block;
         position: absolute;
         left: 70%;
         color: #0be2ff;
         .important{
           font-size: 40px;
           display: inline-block;
           padding-bottom: 1rem;
           font-weight: 500;
           border-bottom: white solid 1px;
         }
         .text{
           font-size: 18px;
           margin: 10px auto;
           width: 100%;
           text-align: center;
           font-weight: 200;
         }
       }
       .btn-wrapper{
         position: absolute;
         left: 75%;
         top: 55%;
         .btn{
           .custom-button();
           &:last-child{
             background: linear-gradient(150deg, rgb(0, 183, 255), rgb(33, 93, 246));
           }
         }
       }
     }
     .line-wrapper{
       height: 10rem;
       width: 100%;
       background: linear-gradient(100deg, #00b8ff, #66eea4);
       display: flex;
       justify-content: center;
       align-items: center;
       text-align: center;
       user-select: none;
       .trait{
         color: white;
         font-size: 2.5rem;
         flex: 0 1 20%;
       }
     }
     .property-wrapper{
       height: 45rem;
       width: 100%;
       background-color: whitesmoke;
       padding-top: 30px;
       h1{
         color: #00b8ff;
         margin: 0;
         padding: 0;
         text-align: center;
         font-size: 3rem;
         font-family: 黑体,serif;
         font-weight: bold;
       }
       .property{
         text-align: center;
         margin: 10px auto;
         width: 60%;
         font-size: 1.4rem;
         color: grey
       }
       .box-wrapper{
         display: flex;
         justify-content: center;
         align-items: center;
         /*background-color: #00b8ff;*/
         height: 80%;
         .box{
           width: 20%;
           height: 55%;
           flex: 0 1 22%;
           margin: 0 2rem;
           border-radius: 10px;
           background: white;
           box-shadow: 10px 10px 10px rgba(0,0,0,0.3);
           .ico{
             margin: 20px;
             background: #00b8ff;
             color: white;
             height: 80px;
             width: 80px;
             border-radius: 10px;
             display: flex;
             justify-content: center;
             align-items: center;
             i{
               font-size: 40px;
               user-select: none;
             }
           }
           .title{
             font-size: 22px;
             margin: 0 20px;
             color: black;
           }
           .text{
             display: inline-block;
             text-align: left;
             color: grey;
             margin: 20px;
             width: 90%;
             font-size: 16px;
           }
         }
       }
     }
     .more{
       background-color: white;
       padding: 10px;
     }
     .other-wrapper{
       height: 40rem;
       width: 100%;
       background-color: whitesmoke;
       background-image: url("../assets/index-display3.jpg");
       background-size: cover;
       .element{
         padding: 12% 0 0 10%;
         user-select: none;
         .text {
           margin-bottom: 20px;
           text-align: left;
           font-size: 3rem;
           font-weight: bold;
           color: white;
         }
         .btn{
           .custom-button();
           width: 300px;
           background: linear-gradient(150deg, rgb(255, 0, 0), rgb(246, 211, 33));
         }
       }
     }
   }

   #about{
     height: 12rem;
     width: 100%;
     background-color: #787878;
     .left{
       height: 100%;
       width: 40%;
       float: left;
       .divide{
         height: 80%;
         margin-top: -22%;
         width: 1px;
         background: white;
         float: right
       }
       .info{
         margin: 30px 5% 0 50%;
         color: white;
         span{
           margin-bottom: 10px;
           display: inline-block;
         }
         span:nth-of-type(1){
           font-size: 26px;
         }
         span:nth-of-type(2){
           color: #d2d2d2;
           font-size: 10px;
           padding-bottom: 10px;
         }
       }
     }
     .right{
       height: 100%;
       width: 60%;
       float: right;
       .list{
         width: 20%;
         margin: 15px 0 0 15px;
         height: 90%;
         display: inline-block;
         .list-title{
           font-size: 18px;
           color: white;
           text-align: center;
           margin-bottom: 10px;
         }
         .list-body{
           display: flex;
           justify-content: center;
           align-items: center;
           flex-direction: column;
         }
         .list-item{
           display: inline-block;
           color: #D2D2D2;
           flex: 1;
           margin-bottom: 5px;
           font-size: 14px;
           cursor: pointer;
           &:hover{
             font-weight: bold;
             color: white;
           }
           &:last-child{
             margin-bottom: 0;
           }
         }
       }
       .support{
         text-align: left;
         margin-left: 30px;
         width: 50%;
         float: right;
         padding: 15px 0 0 0;
         font-weight: bold;
         color: #d2d2d2;
         span{
           display: inline-block;
           margin-top: 5px;
           &:first-child{
             color: white;
             font-size: 20px;
             font-weight: normal;
             margin-top: 0;
           }
         }
       }
     }
   }

   .ripple{
     display: block;
     position: absolute;
     height: 10px;
     width: 10px;
     border-radius: 100%;
     background: rgba(255,255,255,0.5);
     transform: scale(0);
   }
   .animation{
     animation: ripple 0.5s ease-in;
   }
   @keyframes ripple {
     100%{
       transform: scale(3);
       opacity: 0;
     }
   }
   .custom-button(){
     position: relative;
     width: 200px;
     height: 60px;
     border-radius: 10px;
     overflow: hidden;
     display: flex;
     justify-content: center;
     align-items: center;
     color: white;
     text-align: center;
     user-select: none;
     margin: 5px;
     font-size: 20px;
     font-family: 隶书,serif;
     box-shadow: 10px 10px 10px rgba(0,0,0,0.2);
     cursor: pointer;
     background: linear-gradient(150deg, rgb(134, 0, 255), rgb(255, 0, 102));
   }
 }
</style>