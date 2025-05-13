// /mypage/page.js
"use client"
import { useRouter } from "next/navigation";
import CreateSession from './CreateSession';

export default function Mypage(){
    //user.testsessionsをroterまたはfetchで取得し、全てのテストセッションを表示したい
    //バックエンドにはセッションスコープにuserクラスを格納している
const router = useRouter();
const goToTopPage = () => {
router.push("/");
};
    return(
        <div>
            <button onClick={goToTopPage}>
                 トップページへ
                 </button>
            <CreateSession/>
        </div>
    );
}