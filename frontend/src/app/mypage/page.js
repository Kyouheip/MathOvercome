// /mypage/page.js
import CreateSession from './CreateSession';

export default async function Mypage(){
    //user.testsessionsをroterまたはfetchで取得し、全てのテストセッションを表示したい
    //バックエンドにはセッションスコープにuserクラスを格納している
    return(
        <div>
            <CreateSession/>
        </div>
    );
}