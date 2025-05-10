// /mypage/[userId]/CreateSession.js
'use client'
import {useRouter} from 'next/navigation';
import {useState} from 'react';

export default function CreateSession(){
    const [includeIntegers,setIncludeIntegers] = useState(false);
    const [error,setError] = useState(null);
    const router = useRouter();

    const startTest = async () => {
      try{
        const res = await fetch(
            `${process.env.NEXT_PUBLIC_API_URL}/session/test?includeIntegers=${includeIntegers}`,
            {
            method: 'post',
            credentials: 'include',
            }

        );
        
        if(res.status === 401){
            setError("セッションが切れたか不正なアクセスです。ログインし直してください。");
            return ;
        }

        if(!res.ok){
            setError(`送信に失敗しました(${res.status})`);
            return ;
        }
        
        const session = await res.json();
        const idx = 0;
        router.push(`/session/${session.id}/problems/${idx}`);
      }catch (e) {
        setError("通信エラーが発生しました");
             return ;
      }
    };

    if (error) {
        return (
          <div>
            <p>{error}</p>
            <p><a href="/login">ログインページへ</a></p>
          </div>
        );
      }

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