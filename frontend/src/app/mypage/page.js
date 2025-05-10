// /mypage/page.js
"use client"
import CreateSession from './CreateSession';

export default function Mypage(){
    //user.testsessionsをroterまたはfetchで取得し、全てのテストセッションを表示したい
    //バックエンドにはセッションスコープにuserクラスを格納している
    return(
        <div>
            <CreateSession/>
        </div>
    );
}