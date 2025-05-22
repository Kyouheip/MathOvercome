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
        <div className="mb-5">
           <div className="bg-secondary text-white p-4 rounded mb-5">
            <h2 className="mb-3 text-dark">【数IA新規テスト】</h2>
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
      
            <p className="mt-2" style={{ fontSize: "0.9rem" }}>
              ※ 共通テストの数IAでは整数分野は出題されません。
            </p>
          </div>

            <p className="text-white" style={{ fontSize: "1.2rem" }}>
               数IIBC、数IIIは現在準備中です。
            </p>
        </div>
    );
}