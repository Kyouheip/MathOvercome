// /mypage/page.js
"use client"
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import CreateSession from './CreateSession';
import ErrorMessage from '@/components/ErrorMessage';
import { useErrorHandler } from "@/hooks/useErrorHandler"


export default function Mypage(){
    const [user, setUser] = useState(null);
    const [error,setError] = useState(null);
    const errorHandler = useErrorHandler(setError);

    useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/session/mypage`, 
        {
          credentials: "include",
        });

     if (!errorHandler(res)) return;

     const data = await res.json();
          setUser(data);

    } catch (e){
            setError("通信エラーが発生しました。");
            return ;
         }
    };

    //fetchUser();
    },[]);

  if (error) return <ErrorMessage error={error} />;

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