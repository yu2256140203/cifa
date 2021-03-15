!function(t){var n={};function e(r){if(n[r])return n[r].exports;var o=n[r]={i:r,l:!1,exports:{}};return t[r].call(o.exports,o,o.exports,e),o.l=!0,o.exports}e.m=t,e.c=n,e.d=function(t,n,r){e.o(t,n)||Object.defineProperty(t,n,{enumerable:!0,get:r})},e.r=function(t){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},e.t=function(t,n){if(1&n&&(t=e(t)),8&n)return t;if(4&n&&"object"==typeof t&&t&&t.__esModule)return t;var r=Object.create(null);if(e.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:t}),2&n&&"string"!=typeof t)for(var o in t)e.d(r,o,function(n){return t[n]}.bind(null,o));return r},e.n=function(t){var n=t&&t.__esModule?function(){return t.default}:function(){return t};return e.d(n,"a",n),n},e.o=function(t,n){return Object.prototype.hasOwnProperty.call(t,n)},e.p="",e(e.s=1)}([function(t,n){const e={"*":4,"?":4,"+":4,".":3,"|":2,"(":1};t.exports=function(t){const n=[],r=[];for(let e,s=0,i=!1,a=t.length;s<a;s++)if("*"!==(e=t[s])&&"?"!==e&&"+"!==e)if("|"!==e)if("("!==e)if(")"!==e)i&&o("."),n.push(e),i=!0;else{let e;for(;(e=r.pop())&&"("!==e;)n.push(e);if("("!==e)throw new Error(`no "(" match ")" at [${s}] of "${t}"`);i=!0}else i&&o("."),r.push(e),i=!1;else o(e),i=!1;else o(e),i=!0;function o(t){let o;const s=e[t];for(;o=r.pop();){if(!(e[o]>=s)){r.push(o);break}n.push(o)}r.push(t)}let s;for(;s=r.pop();){if("("===s)throw new Error(`not matched "(" of "${t}"`);n.push(s)}return n.join("")}},function(t,n,e){const r=e(2),o=e(3),s=e(0),i=e(4);t.exports={NFA:r,DFA:o,regex2post:s,simulator:i}},function(t,n,e){const r=e(0);class o{constructor(t){this.id=t,this.transitions=[]}}class s{constructor(t,n,e,r){this.id=t,this.from=n,this.to=e,this.input=r}}class i{constructor(t,n){this.start=t,this.end=n}}class a{constructor(){this.start=-1,this.end=-1,this.state_map=new Map,this.transition_map=new Map}toJSON(){const{start:t,end:n,state_map:e,transition_map:r}=this;function o(t){const n=[];for(let e of t)n.push(e);return n}return{start:t,end:n,state_map:o(e),transition_map:o(r)}}travel(t){const n=new Map,{state_map:e,transition_map:r}=this;!function o(s){if(n[s])return;let i=e.get(s);if(!i)return;n[s]=!0;t(i);const a=i.transitions;if(!a)return;let u=r.get(a[0]);if(!u)return;o(u.to);u=r.get(a[1]);if(!u)return;o(u.to)}(this.start)}static createFromRegexp(t){return t?a.createFromPostfixExpression(r(t)):null}static createFromPostfixExpression(t){if("string"!=typeof t)return null;let n=0,e=0;const r=new a,{state_map:u,transition_map:f}=r;function c(){const t=new o(n++);return u.set(t.id,t),t}function p(t,n,r=""){const o=new s(e++,t.id,n.id,r);return t.transitions.push(o.id),f.set(o.id,o),o}const l=[];for(let n,e=0,r=t.length;e<r;e++)switch(n=t[e]){case"|":{const t=l.pop(),n=l.pop(),e=c(),r=c();p(e,n.start),p(e,t.start),p(n.end,r),p(t.end,r),l.push(new i(e,r))}break;case".":{const t=l.pop(),n=l.pop();t.start.transitions.forEach(t=>{f.get(t).from=n.end.id,n.end.transitions.push(t)}),u.delete(t.start.id),l.push(new i(n.start,t.end))}break;case"*":{const t=l.pop(),n=c(),e=c();p(n,t.start),p(n,e),p(t.end,t.start),p(t.end,e),l.push(new i(n,e))}break;case"?":{const t=l.pop(),n=c(),e=c();p(n,t.start),p(n,e),p(t.end,e),l.push(new i(n,e))}break;case"+":{const t=l.pop(),n=c(),e=c();p(n,t.start),p(t.end,t.start),p(t.end,e),l.push(new i(n,e))}break;default:{const t=c(),e=c();p(t,e,n),l.push(new i(t,e))}}const d=l.pop();return r.start=d.start.id,r.end=d.end.id,r}}t.exports=a},function(t,n){t.exports=class{static createFromNFA(t){const{start:n,end:e,state_map:r,transition_map:o}=t,s=function(){const t=new Set;for(let n of o.values())n.input&&t.add(n.input);return t}(),i=new Set,a=new Set,u=[];let f,c=m([n]);for(a.add(c);f=h();){i.add(f);for(let t of s){let n=m(g(f,t));n&&(i.has(n)||a.add(n),u.push({from:f,to:n,input:t}))}}let{end:p,all:l}=function(t,n){let e=new Set;return t.forEach(t=>{const{from:n,to:r}=t;e.add(n),e.add(r)}),{all:e=Array.from(e),end:e.filter(t=>t.split("-").indexOf(""+n)>-1)}}(u,e);return{start:c,end:Array.from(p),inputs:Array.from(s),states:l,transitions:u};function d(t,n){("string"==typeof t?t.split("-"):t).forEach(t=>{const e=r.get(+t);e.transitions.length?e.transitions.forEach(t=>{n(e,o.get(t))}):n(e,null)})}function h(){for(let t of a)if(!i.has(t))return t;return null}function m(t,n){return t?(n=n||new Set,d(t,(t,e)=>{n.add(t.id),!e||e.input||n.has(e.to)||(n.add(e.to),m([e.to],n))}),Array.from(n).sort().join("-")):""}function g(t,n){const e=new Set;return d(t,(t,r)=>{r&&r.input===n&&e.add(r.to)}),Array.from(e).sort().join("-")}}static createFromNFA_step(t,n){const{start:e,end:r,state_map:o,transition_map:s}=t,i=function(){const t=new Set;for(let n of s.values())n.input&&t.add(n.input);return t}(),a=new Set,u=new Set,f=[];let c=d([e]);if(u.add(c),"function"!=typeof n){let t;return n=function({finished:n,data:e,next:r}){n?t=e:r()},t}function p(){return{inputs:i,visited:a,set:u,transitions:f}}function l(t,n){("string"==typeof t?t.split("-"):t).forEach(t=>{const e=o.get(+t);e.transitions.length?e.transitions.forEach(t=>{n(e,s.get(t))}):n(e,null)})}function d(t,n){return t?(n=n||new Set,l(t,(t,e)=>{n.add(t.id),!e||e.input||n.has(e.to)||(n.add(e.to),d([e.to],n))}),Array.from(n).sort().join("-")):""}function h(t,n){const e=new Set;return l(t,(t,r)=>{r&&r.input===n&&e.add(r.to)}),Array.from(e).sort().join("-")}n({finished:!1,data:p(),next:function t(){let e=function(){for(let t of u)if(!a.has(t))return t;return null}();if(e){a.add(e);for(let t of i){let n=d(h(e,t));n&&(a.has(n)||u.add(n),f.push({from:e,to:n,input:t}))}n({finished:!1,data:p(),next:t})}else!function(){let{end:t,all:e}=function(t,n){let e=new Set;return t.forEach(t=>{const{from:n,to:r}=t;e.add(n),e.add(r)}),{all:e=Array.from(e),end:e.filter(t=>t.split("-").indexOf(""+n)>-1)}}(f,r);n({finished:!0,data:{start:c,end:Array.from(t),inputs:Array.from(i),states:e,transitions:f},next:null})}()}})}}},function(t,n){n.run=function(t,n){const{state_map:e,transition_map:r}=t;let o=new Set,s=new Set;a(o,e.get(t.start));for(let t,e=0,r=n.length;e<r;e++){i(t=n[e]);let r=o;o=s,s=r}function i(t){s.clear(),o.forEach(n=>{if(1!==n.transitions.length)return;let o=r.get(n.transitions[0]);o.input===t&&a(s,e.get(o.to))})}function a(t,n){n&&!t.has(n)&&(!function(t){return!!t&&!t.input}(r.get(n.transitions[0]))?t.add(n):(a(t,u(n.transitions[0])),a(t,u(n.transitions[1]))))}function u(t){return void 0===t?null:e.get(r.get(t).to)}for(let t of o)if(0===t.transitions.length)return!0;return!1},n.runWithBacktrack=function(t,n){const{state_map:e,transition_map:r}=t,o=[];let s=0,i=e.get(t.start),a=r.get(i.transitions[0]);const u=1,f=-1,c=0;for(;;){let t=p();if(t===f){if(l()===c)break}else if(t===c)break}function p(){return a?a.input?a.input===n[s]?(s++,o.push(a),i=e.get(a.to),s===n.length?c:(a=r.get(i.transitions[0]),u)):f:(o.push(a),i=e.get(a.to),a=r.get(i.transitions[0]),u):f}function l(){if(!i)return c;const t=i.transitions[1];return void 0===t||t===a.id?(a=o.pop())?(a.input&&s--,i=e.get(a.from),l()):(i=null,c):(a=r.get(t),u)}const d=new Set;!function t(n){if(!n)return;if(d.has(n))return;d.add(n);n.transitions.forEach(n=>{let o=r.get(n);""===o.input&&t(e.get(o.to))})}(i);let h=!1;return d.forEach(t=>{0===t.transitions.length&&(h=!0)}),h}}]);