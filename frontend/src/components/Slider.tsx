import React from 'react';
import * as SliderPrimitive from '@radix-ui/react-slider';
interface SliderProps extends React.ComponentPropsWithoutRef<typeof SliderPrimitive.Root> {
  className?: string;
}
const Slider = React.forwardRef<HTMLSpanElement, SliderProps>(({ className, ...props }, ref) => {
  return (
    <SliderPrimitive.Root
      ref={ref}
      className={`
        relative flex items-center select-none touch-none w-full h-20
        ${className}
      `}
      {...props}
    >
      <SliderPrimitive.Track className="relative h-3 grow rounded-full bg-gray-200">
        {/* Tạo các vạch chia */}
        <div className="absolute top-full pt-2 left-0 right-0 flex justify-between">
          {Array.from({ length: 11 }).map((_, i) => (
            <div key={i} className="flex flex-col items-center">
              <div className="w-0.5 h-2 bg-gray-300" />
              <span className="text-xs text-gray-500 mt-1">{i * 10}</span>
            </div>
          ))}
        </div>
        
        <SliderPrimitive.Range className="absolute h-full rounded-full bg-blue-500" />
      </SliderPrimitive.Track>

      {props.value?.map((_, i) => (
        <SliderPrimitive.Thumb
          key={i}
          className="
            block h-6 w-6 rounded-full border-2 border-blue-500 bg-white
            focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2
            hover:scale-110 transition-transform duration-200
            hover:border-blue-600 shadow-lg
          "
          aria-label={i === 0 ? "Minimum value" : "Maximum value"}
        />
      ))}
    </SliderPrimitive.Root>
  );
});

Slider.displayName = "Slider";

export { Slider };