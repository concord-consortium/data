package org.concord.data.util;


/*****************************************************************************
*
* Version History
* Date                Version  Programmer
* ----------  -------  -------  ------------------------------------------
* 07/20/2001  New      1.0.0    Dmitry Markman, PhD
* Class created
*
****************************************************************************/


public class FFT
{
	private final static int DIM_FFT = 32;
	private static float []sin_fft = null;
	/*
	  private static float []sin_fft = 
	  {
	  1.2246467991473532E-16f,1.0f,0.7071067811865476f,0.3826834323650898f,0.19509032201612828f,
	  0.0980171403295606f,0.049067674327418015f,0.024541228522912288f,0.012271538285719925f,
	  0.006135884649154475f,0.003067956762965976f,0.0015339801862847655f,7.669903187427045E-4f,
	  3.8349518757139556E-4f,1.917475973107033E-4f,9.587379909597734E-5f,4.793689960306688E-5f,
	  2.396844980841822E-5f,1.1984224905069705E-5f,5.9921124526424275E-6f,2.996056226334661E-6f,
	  1.4980281131690111E-6f,7.490140565847157E-7f,3.7450702829238413E-7f,1.8725351414619535E-7f,
	  9.362675707309808E-8f,4.681337853654909E-8f,2.340668926827455E-8f,1.1703344634137277E-8f,
	  5.8516723170686385E-9f,2.9258361585343192E-9f,1.4629180792671596E-9f,
	  };
	*/
	static{
		sin_fft = new float[DIM_FFT];
		float for_fft=2.0f;
		for(int i=0;i<DIM_FFT;i++){
			sin_fft[i]=(float)Math.sin(2.0f*Math.PI/for_fft);
			for_fft*=2.0;
		}
	}
	//output index -> k
	//if k >=2
	//2*freq + 2 for sin;
	//2*freq + 1 for cos;

	/**
	 * FFT implementation from Numerical receipes in C
	 * if isign == 1 then direct FFT
	 * if isign == -1 then inverse FFT
	 *Example:
	 * you should import class Maths
	 *
	 *int	dataDim = 256 ;
	 *float []data = new float[dataDim*2];//you should allocate double size
	 *for(int i = 0; i < dataDim;i++){
	 *	data[i] = Maths.sin(k*(int)i)*Maths.sin(k*(int)i)*Maths.exp(-(float)i/(float)dataDim);
	 *}
	 *FFT.realft(data,dataDim,1);
	 *Frequencies: normalizing: (sqrt(ak*ak+bk*bk)
	 *float []normKoeff = new float[dataDim/2];
	 *for(int i = 1; i <= dataDim;i+=2){
	 *			float nk = Maths.sqrt(data[i]*data[i]+data[i+1]*data[i+1]);
	 *			if(i == 1) nk /= 2.0;//zero k
	 *			normKoeff[(i - 1)/2] = nk;
	 *}
	 * @param a a <code>float[]</code> data.
	 * @param a a <code>int</code> n.
	 * @param a a <code>int</code> isign.
	 */
	static public void realft(float []data,int nn,int isign){
		try{
			int n=nn/2,nstep=0,nTemp=n;
			int i,i1,i2,i3,i4,n2p3;
			float c1=0.5f,c2,h1r,h1i,h2r,h2i;
			float wr,wi,wpr,wpi,wtemp;
			nTemp=n;
			while(nTemp>1){
				nTemp/=2;
				nstep++;
			}
			//	sin_fft[i]=sin(2PI/(2**(i+1)) =  sin(PI/2**i)
			//	theta=3.141592653589793/(float)n;
			if(isign==1){
				c2=-0.5f;
				four1(data,n,1);
				wtemp=sin_fft[nstep+1];
				wpi=sin_fft[nstep];
			}else{
				c2=0.5f;
				//		theta=-theta;
				wtemp=-sin_fft[nstep+1];
				wpi=-sin_fft[nstep];
			}
			//	wtemp=sin(0.5*theta);
			wpr=-2.0f*wtemp*wtemp;
			//	wpi=sin(theta);
			wr=1.0f+wpr;
			wi=wpi;
			n2p3=2*n+3;
			for(i=2;i<=n/2;i++){
				i1=i+i-1;i2=1+i1;i3=n2p3-i2;
				i4=1+i3;
				h1r=c1*(data[i1]+data[i3]);
				h1i=c1*(data[i2]-data[i4]);
				h2r=-c2*(data[i2]+data[i4]);
				h2i=c2*(data[i1]-data[i3]);
				data[i1]=h1r+wr*h2r-wi*h2i;
				data[i2]=h1i+wr*h2i+wi*h2r;
				data[i3]=h1r-wr*h2r+wi*h2i;
				data[i4]=-h1i+wr*h2i+wi*h2r;
				wtemp=wr;
				wr=wtemp*wpr-wi*wpi+wr;
				wi=wi*wpr+wtemp*wpi+wi;
			}
			h1r=data[1];
			if(isign==1){
				data[1]=h1r+data[2];
				data[2]=h1r-data[2];
			}else{
				data[1]=c1*(h1r+data[2]);
				data[2]=c1*(h1r-data[2]);
				four1(data,n,-1);
				for(i=1;i<nn;i++){
					data[i]/=n;
				}
			}
		}catch(Exception e){
			//		    System.out.println("realft "+e);
		}
	}
	static private void SWAP(float []arr,int a,int b){
		try{
			float temp = arr[a];
			arr[a] = arr[b];
			arr[b] = temp;
		}catch(Exception e){
		}
	}

	static private void four1(float  []data,int nn,int isign){
		int n,mmax,m,j,istep,i,nstep;
		float wr,wi,wpr,wpi,wtemp;
		float tempr,tempi;
		n=nn*2;
		j=1;
		for(i=1;i<n;i+=2){
			if(j>i){
				SWAP(data,j,i);
				SWAP(data,j+1,i+1);
			}
			m=n/2;
			while(m>=2 && j>m){
				j-=m;
				m/=2;
			}
			j+=m;
		}
		mmax=2;nstep=0;
		while(n>mmax){
			istep=2*mmax;
			nstep++;
			//			sin_fft[i]=sin(2PI/(2**(i+1)) =  sin(PI/2**i)
			//			theta=6.28318530717959/(float)(isign*mmax);
			if(isign==1){
				wtemp=sin_fft[nstep];
				wpi=sin_fft[nstep-1];
			}else{
				wtemp=-sin_fft[nstep];
				wpi=-sin_fft[nstep-1];
			}
			//			wtemp=sin(0.5*theta);
			wpr=-2.0f*wtemp*wtemp;
			//			wpi=sin(theta);
			wr=1.0f;
			wi=0.0f;
			for(m=1;m<mmax;m+=2){
				for(i=m;i<=n;i+=istep){
					j=i+mmax;
					tempr=wr*data[j]-wi*data[j+1];
					tempi=wr*data[j+1]+wi*data[j];
					data[j]=data[i]-tempr;
					data[j+1]=data[i+1]-tempi;
					data[i]+=tempr;
					data[i+1]+=tempi;
				}
				wtemp=wr;
				wr=wtemp*wpr-wi*wpi+wr;
				wi=wtemp*wpi+wi*wpr+wi;
			}
			mmax=istep;
		}
	}
}
/*
 public static void calcData(FFTJava fftTest,float freq){
 int	dataDim = 256 ;
 float []data = new float[dataDim*2];
 float k = 2.0f*Maths.PI*freq/(float)dataDim;
 for(int i = 0; i < dataDim;i++){
 //			data[i] = Maths.sin(k*(int)i)+Maths.sin(3.0*k*(int)i);
 //			data[i] = 1+Maths.sin(k*(int)i)+4*Maths.cos(2.0*k*(int)i)+4.0*Maths.sin(10.0*k*(int)i);
 //			data[i] = Maths.sin(k*(int)i)+Maths.cos(k*(int)i);
 //			data[i] = Maths.random() - 0.5;
 data[i] = Maths.sin(k*(int)i)*Maths.sin(k*(int)i)*Maths.exp(-(float)i/(float)dataDim);
 }
 FFT.realft(data,dataDim,1);//FFT
 float []normKoeff = new float[dataDim/2];//Frequencies
 for(int i = 1; i <= dataDim;i+=2){
 float nk = Maths.sqrt(data[i]*data[i]+data[i+1]*data[i+1]);
 if(i == 1) nk /= 2.0;
 normKoeff[(i - 1)/2] = nk;
			
 }
 FFT.realft(data,dataDim,-1);//Invers. FFT
 }

*/

