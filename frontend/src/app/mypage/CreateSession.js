// /mypage/[userId]/CreateSession.js
'use client'
import {useRouter} from 'next/navigation';
import {useState} from 'react';
import ErrorMessage from '@/components/ErrorMessage';
import { useErrorHandler } from "@/hooks/useErrorHandler"


export default function CreateSession(){
    const [includeIntegers,setIncludeIntegers] = useState(false);
    const [error,setError] = useState(null);
    const router = useRouter();
    const errorHandler = useErrorHandler(setError);


    const startTest = async () => {
      try{
        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/session/test?includeIntegers=${includeIntegers}`,
            {
            method: 'post',
            credentials: 'include',
            }

        );
        
       if (!errorHandler(res)) return;
        
        const session = await res.json();
        const idx = 0;
        router.push(`/session/${session.id}/problems/${idx}`);
      }catch (e) {
        setError("通信エラーが発生しました");
             return ;
      }
    };

   if (error) return <ErrorMessage error={error} />;


    return(
        <div className="container mt-4">
            <h2 className="mb-3">新規テスト開始</h2>
            <div className="form-check mb-3">
              <input 
                type="checkbox"
                className="form-check-input"
                id="includeIntegers"
                checked={includeIntegers}
                onChange={e => setIncludeIntegers(e.target.checked)}
              />
              <label htmlFor="includeIntegers" className="form-check-label">
                整数分野も問題に含める
              </label>
            </div>
            <button className="btn btn-primary" onClick={startTest}>テスト開始</button>
        </div>
    );
}