// /mypage/page.js
"use client"
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import CreateSession from './CreateSession';

export default function Mypage(){
    const [user, setUser] = useState(null);
    const [error,setError] = useState(null);

    useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/session/mypage`, 
        {
          credentials: "include",
        });

     if(res.status === 401){
                setError("セッションが切れたか不正なアクセスです。ログインし直してください。");
                return ;
     }
    
    if (!res.ok) {
                setError(`エラーが発生しました。: ${res.status}`);
                return;
     }

     const data = await res.json();
          setUser(data);

    } catch (e){
            setError("通信エラーが発生しました。");
            return ;
         }
    };

    //fetchUser();
    },[]);

if (error) {
        return (
          <div>
            <p>{error}</p>
            <p><a href="/login">ログインページへ</a></p>
          </div>
        );
      }

    //  if(!user) return <p>読み込み中...</p>;

    return(
        <div>
            ようこそ～さん
            ログアウト
            sql
            <CreateSession/>
        </div>
    );
}